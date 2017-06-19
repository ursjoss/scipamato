CREATE UNIQUE INDEX idx_search_condition_code_unique ON search_condition_code (search_condition_id, code);
CREATE UNIQUE INDEX idx_search_exclusion_unique ON search_exclusion (search_order_id, paper_id);
CREATE UNIQUE INDEX idx_user_role_unique ON user_role (user_id, role_id);