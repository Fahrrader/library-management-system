from peewee import *

db = SqliteDatabase('library.db')


class Patron(Model):
    name = CharField()
    address = TextField()
    phone = CharField()
    affiliation = CharField(choices=[[0, "faculty"], [1, "student"]])
    _id = IntegerField(primary_key=True)

    class Meta:
        database = db


class Document(Model):
    kind = CharField(choices=[[0, 'book'], [1, 'article'], [2, 'av']])
    author = TextField()
    title = TextField()
    journal = TextField()
    publisher = TextField()
    editor = TextField()
    edition = IntegerField()
    published = DateField()
    keywords = TextField()
    _id = IntegerField(primary_key=True)
    price = IntegerField(constraints=[Check('price>0')])
    taken_by = ForeignKeyField(Patron, backref="documents")
    taken_at = DateField()

    class Meta:
        database = db


def create_tables():
    db.create_tables([Patron, Document])


db.connect()

create_tables()

db.close()
