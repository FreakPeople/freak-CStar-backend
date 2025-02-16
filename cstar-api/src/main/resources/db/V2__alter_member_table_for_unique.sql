ALTER TABLE member ADD CONSTRAINT unique_email UNIQUE (email);
ALTER TABLE member ADD CONSTRAINT unique_nickname UNIQUE (nickname);