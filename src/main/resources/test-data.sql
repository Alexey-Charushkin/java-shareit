ALTER TABLE users
    ALTER COLUMN ID RESTART WITH 1;
INSERT INTO users (name, email)
VALUES ('userName', 'userEmail@mail.com'),
       ('userName2', 'userEmail2@mail.com');

ALTER TABLE requests
    ALTER COLUMN ID RESTART WITH 1;
INSERT INTO requests (description, requestor_id)
VALUES ('requestDescription', 1);


ALTER TABLE items
    ALTER COLUMN ID RESTART WITH 1;
INSERT INTO items (name, description, is_available, owner_id, request_id)
VALUES ('itemName', 'itemDescription', 'true', 1, null),
       ('itemName2', 'itemDescription2', 'true', 1, 1),
       ('itemName3', 'itemDescription3', 'true', 2, 1);


-- bookingRepository.save(Booking.builder()
--                 .start(LocalDateTime.now().plusMinutes(1))
--                 .end(LocalDateTime.now().plusMinutes(5))
--                 .item(item)
--                 .booker(owner)
--                 .status(Booking.Status.WAITING)
--                 .build());
--
-- bookingRepository.save(Booking.builder()
--                 .start(LocalDateTime.now().plusMinutes(6))
--                 .end(LocalDateTime.now().plusMinutes(10))
--                 .item(item)
--                 .booker(owner)
--                 .status(Booking.Status.APPROVED)
--                 .build());
--
-- bookingRepository.save(Booking.builder()
--                 .start(LocalDateTime.now().plusMinutes(11))
--                 .end(LocalDateTime.now().plusMinutes(15))
--                 .item(item2)
--                 .booker(requestor)
--                 .status(Booking.Status.REJECTED)
--                 .build());
--
-- bookingRepository.save(Booking.builder()
--                 .start(LocalDateTime.now().plusMinutes(16))
--                 .end(LocalDateTime.now().plusMinutes(20))
--                 .item(item)
--                 .booker(requestor)
--                 .status(Booking.Status.APPROVED)
--                 .build());
