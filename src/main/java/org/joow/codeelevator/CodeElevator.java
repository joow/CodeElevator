package org.joow.codeelevator;

import com.google.common.base.Optional;
import static spark.Spark.*;

import org.joow.elevator2.Direction;
import org.joow.elevator2.ElevatorController;
import spark.*;

public class CodeElevator {
    private static final String DEFAULT_PORT = "9000";

    public static void main(String[] args) {
        final int port = Integer.valueOf(Optional.fromNullable(System.getenv("PORT")).or(DEFAULT_PORT));
        setPort(port);

        final ElevatorController elevatorController = new ElevatorController();

        get(new Route("/call") {
            @Override
            public Object handle(Request request, Response response) {
                final int atFloor = Integer.parseInt(request.queryParams("atFloor"));
                final Direction to = Direction.of(request.queryParams("to"));
                elevatorController.callAt(atFloor, to);

                return "";
            }
        });

        get(new Route("/go") {
            @Override
            public Object handle(Request request, Response response) {
                final int floorToGo = Integer.parseInt(request.queryParams("floorToGo"));
                elevatorController.go(floorToGo);

                return "";
            }
        });

        get(new Route("/userHasEntered") {
            @Override
            public Object handle(Request request, Response response) {
                elevatorController.userHasEntered();
                return "";
            }
        });

        get(new Route("/userHasExited") {
            @Override
            public Object handle(Request request, Response response) {
                elevatorController.userHasExited();
                return "";
            }
        });

        get(new Route("/reset") {
            @Override
            public Object handle(Request request, Response response) {
                elevatorController.reset();

                return "";
            }
        });

        get(new Route("/nextCommand") {
            @Override
            public Object handle(Request request, Response response) {
                try {
                    return elevatorController.nextCommand().toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                return "";
            }
        });
    }
}
