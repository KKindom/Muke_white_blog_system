package com.zsc.blog.web.Interceptor;

import com.alibaba.fastjson.JSON;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.zsc.blog.config.MyException;
import com.zsc.blog.config.PassToken;
import com.zsc.blog.config.UserLoginToken;
import com.zsc.blog.entity.TUser;
import com.zsc.blog.service.ITUserService;
import org.apache.tomcat.util.http.MimeHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class AuthenticationInterceptor implements HandlerInterceptor {
    @Autowired
    ITUserService itUserService;
    @Autowired
    RedisTemplate redisTemplate;
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws MyException  {
        String token = httpServletRequest.getHeader("accessToken");// 从 http 请求头中取出 token
        System.out.println("dsa"+token);
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        HandlerMethod handlerMethod = (HandlerMethod) object;
        Method method = handlerMethod.getMethod();
        //检查是否有passtoken注释，有则跳过认证
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.required()) {
                return true;
            }
        }
        //检查有没有需要用户权限的注解
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.required()) {
                // 执行认证
                if (token == null) {
                    throw new MyException("无token，请重新登录");
                }
                // 获取 token 中的 user id
                String userId , permission;
                Algorithm algorithm =null;
                DecodedJWT verify =null;
                algorithm = Algorithm.HMAC256("jung");
                JWTVerifier verifier = JWT.require(algorithm)
                        .withIssuer("jung")
                        .build();
                //取token里面的data里面的用户信息
                verify = verifier.verify(token);
                String dataString = verify.getClaim("data").asString();
                 userId =  JSON.parseObject(dataString).getString("id");
                permission = JSON.parseObject(dataString).getString("permission");
                Object obj = redisTemplate.opsForValue().get("token_"+userId);
                if(obj==null){

                    TUser user = itUserService.selectById(userId);
                    System.out.println(user);
                    if (user == null) {
                        //return false;
                        throw new MyException("用户不存在，请重新登录");
                    }
                    redisTemplate.opsForValue().set("token_"+userId,user);
                }

                httpServletRequest.getServletContext().setAttribute("userId",userId);
                httpServletRequest.getServletContext().setAttribute("permission",permission);
                return true;
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest,
                           HttpServletResponse httpServletResponse,
                           Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest,
                                HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        //System.out.println("拦截器之后输出ServletContext中保存的数据："+httpServletRequest.getServletContext().getAttribute("userId"));
    }
    private void reflectSetHeader(HttpServletRequest request, String key, String value){
        Class<? extends HttpServletRequest> requestClass = request.getClass();
        try {
            Field request1 = requestClass.getDeclaredField("request");
            request1.setAccessible(true);
            Object o = request1.get(request);
            Field coyoteRequest = o.getClass().getDeclaredField("coyoteRequest");
            coyoteRequest.setAccessible(true);
            Object o1 = coyoteRequest.get(o);
            Field headers = o1.getClass().getDeclaredField("headers");
            headers.setAccessible(true);
            MimeHeaders o2 = (MimeHeaders)headers.get(o1);
            o2.removeHeader(key);
            o2.addValue(key).setString(value);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}