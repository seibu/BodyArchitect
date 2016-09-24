create table if not exists FOOD(
	id integer primary key autoincrement,
	name VARCHAR NOT NULL,
	weight DOUBLE NOT NULL,
	portion DOUBLE NOT NULL,
	kcal DOUBLE,
	protein DOUBLE,
	fat DOUBLE,
	carbs DOUBLE,
	sugar DOUBLE);
	
create table if not exists MEAL(
	id integer primary key autoincrement,
	type varchar not null);
	
create table if not exists MEAL_FOOD(
	id integer primary key autoincrement,
	f_id integer REFERENCES FOOD(id),
	m_id integer REFERENCES MEAL(id),
	weight DOUBLE NOT NULL);
	
create table if not exists DAYS(
	id integer primary key autoincrement,
	"date" date not null);
	
create table if not exists DAY_MEAL(
	id integer primary key autoincrement,
	d_id integer REFERENCES DAYS(id),
	m_id integer REFERENCES MEAL(id));

create table if not exists DAY_FOOD(
  id integer primary key autoincrement,
  d_id integer REFERENCES DAYS(id),
  f_id integer REFERENCES FOOD(id),
  weight DOUBLE NOT NULL
);