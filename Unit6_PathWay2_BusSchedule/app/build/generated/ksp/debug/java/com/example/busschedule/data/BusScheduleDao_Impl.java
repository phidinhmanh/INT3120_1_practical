package com.example.busschedule.data;

import android.database.Cursor;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class BusScheduleDao_Impl implements BusScheduleDao {
  private final RoomDatabase __db;

  private final EntityInsertionAdapter<BusSchedule> __insertionAdapterOfBusSchedule;

  private final EntityDeletionOrUpdateAdapter<BusSchedule> __deletionAdapterOfBusSchedule;

  private final EntityDeletionOrUpdateAdapter<BusSchedule> __updateAdapterOfBusSchedule;

  public BusScheduleDao_Impl(RoomDatabase __db) {
    this.__db = __db;
    this.__insertionAdapterOfBusSchedule = new EntityInsertionAdapter<BusSchedule>(__db) {
      @Override
      public String createQuery() {
        return "INSERT OR ABORT INTO `bus_schedule` (`id`,`stopName`,`arrivalTimeInMillis`) VALUES (nullif(?, 0),?,?)";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BusSchedule value) {
        stmt.bindLong(1, value.getId());
        if (value.getStopName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getStopName());
        }
        stmt.bindLong(3, value.getArrivalTimeInMillis());
      }
    };
    this.__deletionAdapterOfBusSchedule = new EntityDeletionOrUpdateAdapter<BusSchedule>(__db) {
      @Override
      public String createQuery() {
        return "DELETE FROM `bus_schedule` WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BusSchedule value) {
        stmt.bindLong(1, value.getId());
      }
    };
    this.__updateAdapterOfBusSchedule = new EntityDeletionOrUpdateAdapter<BusSchedule>(__db) {
      @Override
      public String createQuery() {
        return "UPDATE OR ABORT `bus_schedule` SET `id` = ?,`stopName` = ?,`arrivalTimeInMillis` = ? WHERE `id` = ?";
      }

      @Override
      public void bind(SupportSQLiteStatement stmt, BusSchedule value) {
        stmt.bindLong(1, value.getId());
        if (value.getStopName() == null) {
          stmt.bindNull(2);
        } else {
          stmt.bindString(2, value.getStopName());
        }
        stmt.bindLong(3, value.getArrivalTimeInMillis());
        stmt.bindLong(4, value.getId());
      }
    };
  }

  @Override
  public Object insert(final BusSchedule busSchedules,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __insertionAdapterOfBusSchedule.insert(busSchedules);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object delete(final BusSchedule busSchedule,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __deletionAdapterOfBusSchedule.handle(busSchedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Object update(final BusSchedule busSchedule,
      final Continuation<? super Unit> continuation) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __updateAdapterOfBusSchedule.handle(busSchedule);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, continuation);
  }

  @Override
  public Flow<List<BusSchedule>> getAll() {
    final String _sql = "SELECT * FROM bus_schedule ORDER BY ArrivalTimeInMillis ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[]{"bus_schedule"}, new Callable<List<BusSchedule>>() {
      @Override
      public List<BusSchedule> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStopName = CursorUtil.getColumnIndexOrThrow(_cursor, "stopName");
          final int _cursorIndexOfArrivalTimeInMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "arrivalTimeInMillis");
          final List<BusSchedule> _result = new ArrayList<BusSchedule>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BusSchedule _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpStopName;
            if (_cursor.isNull(_cursorIndexOfStopName)) {
              _tmpStopName = null;
            } else {
              _tmpStopName = _cursor.getString(_cursorIndexOfStopName);
            }
            final int _tmpArrivalTimeInMillis;
            _tmpArrivalTimeInMillis = _cursor.getInt(_cursorIndexOfArrivalTimeInMillis);
            _item = new BusSchedule(_tmpId,_tmpStopName,_tmpArrivalTimeInMillis);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Flow<List<BusSchedule>> getForStopName(final String stopName) {
    final String _sql = "SELECT * FROM bus_schedule WHERE stopName = ? ORDER BY ArrivalTimeInMillis ASC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    if (stopName == null) {
      _statement.bindNull(_argIndex);
    } else {
      _statement.bindString(_argIndex, stopName);
    }
    return CoroutinesRoom.createFlow(__db, false, new String[]{"bus_schedule"}, new Callable<List<BusSchedule>>() {
      @Override
      public List<BusSchedule> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfId = CursorUtil.getColumnIndexOrThrow(_cursor, "id");
          final int _cursorIndexOfStopName = CursorUtil.getColumnIndexOrThrow(_cursor, "stopName");
          final int _cursorIndexOfArrivalTimeInMillis = CursorUtil.getColumnIndexOrThrow(_cursor, "arrivalTimeInMillis");
          final List<BusSchedule> _result = new ArrayList<BusSchedule>(_cursor.getCount());
          while(_cursor.moveToNext()) {
            final BusSchedule _item;
            final int _tmpId;
            _tmpId = _cursor.getInt(_cursorIndexOfId);
            final String _tmpStopName;
            if (_cursor.isNull(_cursorIndexOfStopName)) {
              _tmpStopName = null;
            } else {
              _tmpStopName = _cursor.getString(_cursorIndexOfStopName);
            }
            final int _tmpArrivalTimeInMillis;
            _tmpArrivalTimeInMillis = _cursor.getInt(_cursorIndexOfArrivalTimeInMillis);
            _item = new BusSchedule(_tmpId,_tmpStopName,_tmpArrivalTimeInMillis);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
