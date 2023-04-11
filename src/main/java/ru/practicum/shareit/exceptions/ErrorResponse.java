package ru.practicum.shareit.exceptions;

public class ErrorResponse {

        private final String description;
        public ErrorResponse(String description) {
            this.description = description;
        }

        public String getDescription() {
            return description;
        }


}
