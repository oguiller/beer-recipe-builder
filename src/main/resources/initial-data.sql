insert into APP_USER(ID,  FIRST_NAME, SURNAME, USERNAME,  PASSWORD, MAIL, PHONE) values(1,'Guillermo','Rodriguez','xile','$2a$10$bnC26zz//2cavYoSCrlHdecWF8tkGfPodlHcYwlACBBwJvcEf0p2G', 'guillermo@oguiller.com', '+31678876789');
insert into USER_ROLE(APP_USER_ID, ROLE) values(1, 'ADMIN');
insert into USER_ROLE(APP_USER_ID, ROLE) values(1, 'PREMIUM_MEMBER');

INSERT INTO RECIPE (ID, NAME, STYLE, TYPE, UNITS) VALUES (1, 'I.P.A', 'Indian Pale Ale', 'ALL_GRAIN', 'METRIC');
insert into HOP(ID, NAME, COUNTRY) values(1,'Cascade', 'USA');

insert into FERMENTABLE(ID, COLOR, NAME, TYPE) values (1, '1.7 SRM', 'Flaked Barley', 'Grain');
insert into FERMENTABLE(ID, COLOR, NAME, TYPE) values (2, '1.3 SRM', 'Flaked Corn', 'Grain');

insert into YEAST(ID, ATTENUATION, LABORATORY, MAX_TEMP, MIN_TEMP) values (1, '82.0%-82.0%', 'Ferments', 68.0, 59.0);

insert into RECIPE_HOPS(RECIPE_ID, HOPS_ID) values (1, 1);
insert into RECIPE_FERMENTABLES(RECIPE_ID, FERMENTABLES_ID) values (1,1);
insert into RECIPE_FERMENTABLES(RECIPE_ID, FERMENTABLES_ID) values (1, 2);
insert into RECIPE_YEASTS(RECIPE_ID, YEASTS_ID) values (1,1);