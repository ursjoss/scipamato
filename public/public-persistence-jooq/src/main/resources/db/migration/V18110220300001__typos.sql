UPDATE code_class
set name = 'Type d''étude'
where lang_code = 'fr'
  and name = 'Typ d''étude';

UPDATE keyword
set name = 'Allergie'
where lang_code = 'fr'
  and name = 'Allérgie';

UPDATE keyword
set name = replace(name, 'É', 'E')
where lang_code = 'fr' and name like '%É%';