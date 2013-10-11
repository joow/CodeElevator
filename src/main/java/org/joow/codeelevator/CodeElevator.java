package org.joow.codeelevator;

import org.joow.elevator.Direction;
import org.joow.elevator.ElevatorEngine;
import static spark.Spark.*;
import spark.*;

public class CodeElevator {
    private static final int DEFAULT_PORT = 9000;

    public static void main(String[] args) {
        setPort(getPort());

        final ElevatorEngine elevatorEngine = new ElevatorEngine();

        get(new Route("/call") {
            @Override
            public Object handle(Request request, Response response) {
                final int atFloor = Integer.parseInt(request.queryParams("atFloor"));
                final Direction to = Direction.of(request.queryParams("to"));
                elevatorEngine.callAt(atFloor, to);

                return "";
            }
        });

        get(new Route("/go") {
            @Override
            public Object handle(Request request, Response response) {
                final int floorToGo = Integer.parseInt(request.queryParams("floorToGo"));
                elevatorEngine.go(floorToGo);

                return "";
            }
        });

        get(new Route("/userHasEntered") {
            @Override
            public Object handle(Request request, Response response) {
                return "";
            }
        });

        get(new Route("/userHasExited") {
            @Override
            public Object handle(Request request, Response response) {
                return "";
            }
        });

        get(new Route("/reset") {
            @Override
            public Object handle(Request request, Response response) {
                elevatorEngine.reset();

                return "";
            }
        });

        get(new Route("/nextCommand") {
            @Override
            public Object handle(Request request, Response response) {
                return elevatorEngine.nextCommand();
            }
        });
    }

    private static int getPort() {
        String port = System.getenv("PORT");

        if (port == null) {
            return DEFAULT_PORT;
        }

        return Integer.valueOf(port);
    }
}
