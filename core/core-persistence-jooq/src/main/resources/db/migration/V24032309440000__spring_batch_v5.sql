ALTER TABLE IF EXISTS batch_job_execution
    DROP COLUMN IF EXISTS job_configuration_location;

ALTER TABLE IF EXISTS  batch_step_execution
    ADD IF NOT EXISTS create_time TIMESTAMP NOT NULL DEFAULT '1970-01-01 00:00:00';

ALTER TABLE IF EXISTS  batch_step_execution
    ALTER COLUMN start_time DROP NOT NULL;

ALTER TABLE IF EXISTS  batch_job_execution_params
    DROP COLUMN IF EXISTS date_val;

ALTER TABLE IF EXISTS  batch_job_execution_params
    DROP COLUMN IF EXISTS long_val;

ALTER TABLE IF EXISTS  batch_job_execution_params
    DROP COLUMN IF EXISTS double_val;

ALTER TABLE IF EXISTS  batch_job_execution_params
    ALTER COLUMN type_cd TYPE VARCHAR(100);

ALTER TABLE IF EXISTS  batch_job_execution_params
    RENAME type_cd TO parameter_type;

ALTER TABLE IF EXISTS  batch_job_execution_params
    ALTER COLUMN key_name TYPE VARCHAR(100);

ALTER TABLE IF EXISTS  batch_job_execution_params
    RENAME key_name TO parameter_name;

ALTER TABLE IF EXISTS  batch_job_execution_params
    ALTER COLUMN string_val TYPE VARCHAR(2500);

ALTER TABLE IF EXISTS  batch_job_execution_params
    RENAME string_val TO parameter_value;
