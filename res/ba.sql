/* Copyright 2016 Sebastian Meyer (seibushin.de)
 *
 * NO LICENSE
 * YOU MAY NOT REPRODUCE, DISTRIBUTE, OR CREATE DERIVATIVE WORKS FROM MY WORK
 *
 */

CREATE TABLE FOODS (
  'id' int NOT NULL AUTO_INCREMENT,
  'name' VARCHAR(50) NOT NULL,
  'kcal' int NOT NULL,
  'protein' int NOT NULL,
  'carbs' int NOT NULL,
  'sugar' int NOT NULL,
  'fat' int NOT NULL,
  'weight' int NOT NULL,
  'portion' int NOT NULL,
  PRIMARY KEY ('id')
)

CREATE TABLE MEALS (
  'id' int NOT NULL AUTO_INCREMENT,
  'name' VARCHAR(50) NOT NULL,
  'type' VARCHAR(50) NOT NULL,
  PRIMARY KEY ('id')
)

CREATE TABLE MEAL_FOODS (
  'id' int NOT NULL,
  'id' int NOT NULL,
  'weight' int NOT NULL,
  PRIMARY KEY ('id', 'id'),
  INDEX 'FK_MEAL' ('id'),
  CONSTRAINT 'FK_MEAL' FOREIGN KEY ('id') REFERENCES 'MEALS' ('id'),
  CONSTRAINT 'FK_FOOD' FOREIGN KEY ('id') REFERENCES 'FOODS' ('id'),
)