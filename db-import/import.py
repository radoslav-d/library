import csv
import os

from sqlalchemy import create_engine
from sqlalchemy.orm import scoped_session, sessionmaker

def main():
    # in cmd or git bash execute export DATABASE_URL={database url}
    dbUrl = os.getenv("DATABASE_URL")
    if not dbUrl:
        raise RuntimeError("DATABASE_URL is not set")

    engine = create_engine(dbUrl)
    db = scoped_session(sessionmaker(bind=engine))

    createUserTable(db)
    createBookTable(db)
    loadBooks(db)

def createUserTable(db):
    db.execute("""CREATE TABLE users (
    userId SERIAL PRIMARY KEY,
    username VARCHAR NOT NULL,
    hash VARCHAR NOT NULL
    );""")
    db.commit()
    print("Users table created")

def createBookTable(db):
    db.execute("""CREATE TABLE books (
    id SERIAL PRIMARY KEY,
    isbn VARCHAR NOT NULL,
    title VARCHAR NOT NULL,
    author VARCHAR NOT NULL,
    year VARCHAR NOT NULL,
    isTaken BOOLEAN DEFAULT FALSE,
    takenBy VARCHAR,
    takenOn DATE,
    returnedOn DATE
    );""")
    db.commit()
    print("Books table created")

def loadBooks(db):
    fp = open("books.csv")
    reader = csv.reader(fp)
    for isbn, title, author, year in reader:
        db.execute("INSERT INTO books (isbn, title, author, year) VALUES (:isbn, :title, :author, :year)",
        {"isbn": isbn, "title":title, "author":author, "year":year})
        print(f'Book loaded: {isbn}, {title}, {author}, {year}')
    db.commit()
    print("Books data loaded")

if __name__=="__main__":
    main()