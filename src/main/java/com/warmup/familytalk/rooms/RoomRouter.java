package com.warmup.familytalk.rooms;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@EnableWebFlux
@Configuration
public class RoomRouter {

    static final String ROOMS_URL = "/rooms";

    @Bean
    public RouterFunction<ServerResponse> rooms(CreateRoomHandler createRoomHandler,
                                                FindRoomHandler findRoomHandler,
                                                RemoveRoomHandler removeRoomHandler) {
        return route(POST(ROOMS_URL), createRoomHandler::handle)
                .andRoute(GET(ROOMS_URL), findRoomHandler::handle)
                .andRoute(DELETE(ROOMS_URL), removeRoomHandler::handle);
    }
}
