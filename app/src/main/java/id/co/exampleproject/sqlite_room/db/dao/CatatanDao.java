package id.co.exampleproject.sqlite_room.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import id.co.exampleproject.sqlite_room.db.entity.Catatan;

@Dao
public interface CatatanDao {
    @Insert
    void create(Catatan catatan);

    @Update
    void update(Catatan catatan);

    @Delete
    void delete(Catatan catatan);

    @Query("SELECT * FROM tbcatatan")
    List<Catatan> getAll();
}
