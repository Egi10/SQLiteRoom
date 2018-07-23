package id.co.exampleproject.sqlite_room.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import id.co.exampleproject.sqlite_room.db.dao.CatatanDao;
import id.co.exampleproject.sqlite_room.db.entity.Catatan;

@Database(entities = {Catatan.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CatatanDao getCatatanDao();
}
