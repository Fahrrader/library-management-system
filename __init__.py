from peewee import *

db = SqliteDatabase('library.db')


class User(Model):
    name = CharField()
    address = TextField()
    phone = CharField()
    affiliation = CharField(choices=[[0, "librarian"], [1, "faculty"], [2, "student"]])
    _id = IntegerField(primary_key=True)

    class Meta:
        database = db


class Document(Model):
    kind = CharField(choices=[[0, 'book'], [1, 'article'], [2, 'av']])
    reference = BooleanField(default=False)
    author = TextField()
    title = TextField()
    journal = TextField(default="")
    publisher = TextField(default="")
    editor = TextField(default="")
    edition = IntegerField(default=1)
    published = DateField(default="0-1-1")
    keywords = TextField(default="")
    location = TextField(default="No")
    _id = IntegerField(primary_key=True)
    price = IntegerField(constraints=[Check('price>=0')], default=0)
    taken_by = ForeignKeyField(User, backref="documents", default=0)
    taken_at = DateField(default="0-0-0")

    class Meta:
        database = db


db.connect()

db.create_tables([User, Document])

db.close()
