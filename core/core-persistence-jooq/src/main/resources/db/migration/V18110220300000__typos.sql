UPDATE code_class_tr
set name = 'Type d''étude'
where lang_code = 'fr'
  and name = 'Typ d''étude';

UPDATE keyword_tr
set name = 'Allergie'
where lang_code = 'fr'
  and name = 'Allérgie';

UPDATE keyword_tr
set name = replace(name, 'É', 'E')
where lang_code = 'fr' and name like '%É%';