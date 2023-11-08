truncate drone cascade;
truncate medication cascade;

INSERT INTO drone.drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DRONE-TEST-02', 'LIGHT_WEIGHT', 100, 100, 'DELIVERING');
INSERT INTO drone.drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DRONE-TEST-03', 'MIDDLE_WEIGHT', 300, 25, 'IDLE');
INSERT INTO drone.drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DRONE-TEST-04', 'MIDDLE_WEIGHT', 300, 20, 'IDLE');
INSERT INTO drone.drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DRONE-TEST-01', 'CRUISER_WEIGHT', 400, 100, 'LOADING');
INSERT INTO drone.drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DRONE-TEST-08', 'HEAVY_WEIGHT', 500, 80, 'RETURNING');
INSERT INTO drone.drone (serial_number, model, weight_limit, battery_capacity, state)
VALUES ('DRONE-TEST-09', 'LIGHT_WEIGHT', 100, 100, 'IDLE');

INSERT INTO drone.medication (code, "name", weight, image)
VALUES ('MEDICATION_TEST_01', 'ASPIRIN', 10, 'https://s3.amazonaws.com/bucket/1.png');
INSERT INTO drone.medication (code, "name", weight, image)
VALUES ('MEDICATION_TEST_02', 'VITAMIN', 10, 'https://s3.amazonaws.com/bucket/2.png');
INSERT INTO drone.medication (code, "name", weight, image)
VALUES ('MEDICATION_TEST_03', 'MELATONIN', 50, 'https://s3.amazonaws.com/bucket/3.png');
INSERT INTO drone.medication (code, "name", weight, image)
VALUES ('MEDICATION_TEST_04', 'BISACODIL', 100, 'https://s3.amazonaws.com/bucket/4.png');
INSERT INTO drone.medication (code, "name", weight, image)
VALUES ('MEDICATION_TEST_05', 'GUTALAKS', 150, 'https://s3.amazonaws.com/bucket/5.png');
INSERT INTO drone.medication (code, "name", weight, image)
VALUES ('MEDICATION_TEST_06', 'FITOLAKS', 400, 'https://s3.amazonaws.com/bucket/6.png');

INSERT INTO drone.drone_medication (drone_id, medication_id)
VALUES ('DRONE-TEST-02', 'MEDICATION_TEST_01');
INSERT INTO drone.drone_medication (drone_id, medication_id)
VALUES ('DRONE-TEST-02', 'MEDICATION_TEST_02');
INSERT INTO drone.drone_medication (drone_id, medication_id)
VALUES ('DRONE-TEST-02', 'MEDICATION_TEST_02');
INSERT INTO drone.drone_medication (drone_id, medication_id)
VALUES ('DRONE-TEST-02', 'MEDICATION_TEST_01');
INSERT INTO drone.drone_medication (drone_id, medication_id)
VALUES ('DRONE-TEST-01', 'MEDICATION_TEST_05');
