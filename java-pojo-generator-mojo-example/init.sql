CREATE SCHEMA IF NOT EXISTS test_schema;
SET SCHEMA test_schema;

CREATE TABLE IF NOT EXISTS test_schema.test_table_1
(
    simple_id_1   INT AUTO_INCREMENT PRIMARY KEY,
    test_varchar  VARCHAR(50)    NOT NULL,
    test_numeric  NUMERIC(10, 2) NOT NULL,
    test_date     DATE           NOT NULL,
    test_datetime TIMESTAMP      NOT NULL
);
COMMENT ON TABLE test_schema.test_table_1 IS 'test_table_1 description';

CREATE TABLE IF NOT EXISTS test_schema.test_table_2
(
    simple_id_2   INT AUTO_INCREMENT PRIMARY KEY,
    test_varchar  VARCHAR(50),
    test_numeric  NUMERIC(10, 2),
    test_date     DATE,
    test_datetime TIMESTAMP,
    FOREIGN KEY (simple_id_2) REFERENCES test_schema.test_table_1 (simple_id_1)
);
COMMENT ON COLUMN test_schema.test_table_2.simple_id_2 IS 'this is table2 id';

CREATE TABLE IF NOT EXISTS test_schema.test_table_3
(
    simple_id_1 INT NOT NULL,
    simple_id_2 INT NOT NULL,
    FOREIGN KEY (simple_id_1) REFERENCES test_schema.test_table_1 (simple_id_1),
    FOREIGN KEY (simple_id_2) REFERENCES test_schema.test_table_2 (simple_id_2)
);
