package com.zero.summer.webclient.filter;

import com.zero.summer.core.constant.SecurityConstant;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

/**
 * @author Zero.
 * @date 2023/2/17 12:25 PM
 */
public class TokenFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        return  next.exchange(ClientRequest.from(request).headers(h -> h.add(SecurityConstant.TOKEN_KEY, "Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsImlhdCI6MTY3NjYwNDU3NCwiZXhwIjoxNjc3NDY4NTc0fQ.Sf1t1bw0Gh0F8QyMS64CPIWJmfVEeEzllBWsGdrVNIo")).build());
//        Optional<Object> token = request.attribute(SecurityConstant.TOKEN_KEY);
//        Object o1 = request.attributes().get(SecurityConstant.TOKEN_KEY);
//        String o = (String) token.get();
//        if (o == null){
//            return next.exchange(request);
//        }else {
//
//            return next.exchange(build);
//        }
    }
}
