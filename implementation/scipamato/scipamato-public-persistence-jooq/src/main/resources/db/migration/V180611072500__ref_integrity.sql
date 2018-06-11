ALTER TABLE code_class ADD FOREIGN KEY (code_class_id, lang_code) REFERENCES code_class(code_class_id, lang_code)
on delete cascade on update cascade;
