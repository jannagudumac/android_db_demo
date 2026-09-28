package com.example.persistencedemo.data;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.room.EntityDeleteOrUpdateAdapter;
import androidx.room.EntityInsertAdapter;
import androidx.room.RoomDatabase;
import androidx.room.util.DBUtil;
import androidx.room.util.SQLiteStatementUtil;
import androidx.sqlite.SQLiteStatement;
import java.lang.Class;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.annotation.processing.Generated;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation", "removal"})
public final class TaskDao_Impl implements TaskDao {
  private final RoomDatabase __db;

  private final EntityInsertAdapter<Task> __insertAdapterOfTask;

  private final EntityDeleteOrUpdateAdapter<Task> __deleteAdapterOfTask;

  private final EntityDeleteOrUpdateAdapter<Task> __updateAdapterOfTask;

  public TaskDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__insertAdapterOfTask = new EntityInsertAdapter<Task>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT OR ABORT INTO `tasks` (`id`,`title`,`completed`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Task entity) {
        statement.bindLong(1, entity.id);
        if (entity.title == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.title);
        }
        final int _tmp = entity.completed ? 1 : 0;
        statement.bindLong(3, _tmp);
      }
    };
    this.__deleteAdapterOfTask = new EntityDeleteOrUpdateAdapter<Task>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "DELETE FROM `tasks` WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Task entity) {
        statement.bindLong(1, entity.id);
      }
    };
    this.__updateAdapterOfTask = new EntityDeleteOrUpdateAdapter<Task>() {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE OR ABORT `tasks` SET `id` = ?,`title` = ?,`completed` = ? WHERE `id` = ?";
      }

      @Override
      protected void bind(@NonNull final SQLiteStatement statement, final Task entity) {
        statement.bindLong(1, entity.id);
        if (entity.title == null) {
          statement.bindNull(2);
        } else {
          statement.bindText(2, entity.title);
        }
        final int _tmp = entity.completed ? 1 : 0;
        statement.bindLong(3, _tmp);
        statement.bindLong(4, entity.id);
      }
    };
  }

  @Override
  public long insert(final Task task) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      return __insertAdapterOfTask.insertAndReturnId(_connection, task);
    });
  }

  @Override
  public int delete(final Task task) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      int _result = 0;
      _result += __deleteAdapterOfTask.handle(_connection, task);
      return _result;
    });
  }

  @Override
  public int update(final Task task) {
    return DBUtil.performBlocking(__db, false, true, (_connection) -> {
      int _result = 0;
      _result += __updateAdapterOfTask.handle(_connection, task);
      return _result;
    });
  }

  @Override
  public LiveData<List<Task>> observeAll() {
    final String _sql = "SELECT * FROM tasks ORDER BY id DESC";
    return __db.getInvalidationTracker().createLiveData(new String[] {"tasks"}, false, (_connection) -> {
      final SQLiteStatement _stmt = _connection.prepare(_sql);
      try {
        final int _columnIndexOfId = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "id");
        final int _columnIndexOfTitle = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "title");
        final int _columnIndexOfCompleted = SQLiteStatementUtil.getColumnIndexOrThrow(_stmt, "completed");
        final List<Task> _result = new ArrayList<Task>();
        while (_stmt.step()) {
          final Task _item;
          final String _tmpTitle;
          if (_stmt.isNull(_columnIndexOfTitle)) {
            _tmpTitle = null;
          } else {
            _tmpTitle = _stmt.getText(_columnIndexOfTitle);
          }
          _item = new Task(_tmpTitle);
          _item.id = _stmt.getLong(_columnIndexOfId);
          final int _tmp;
          _tmp = (int) (_stmt.getLong(_columnIndexOfCompleted));
          _item.completed = _tmp != 0;
          _result.add(_item);
        }
        return _result;
      } finally {
        _stmt.close();
      }
    });
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
