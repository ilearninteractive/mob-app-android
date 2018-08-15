package sa.gov.moe.etraining.module.db.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import sa.gov.moe.etraining.model.VideoModel;
import sa.gov.moe.etraining.module.db.DatabaseModelFactory;

class DbOperationGetVideos extends DbOperationSelect<List<VideoModel>> {
    
    DbOperationGetVideos(boolean distinct, String table, String[] columns,
            String whereClause, String[] whereArgs, String orderBy) {
        super(distinct, table, columns, whereClause, whereArgs, orderBy);
    }
    
    @Override
    public List<VideoModel> execute(SQLiteDatabase db) {
        List<VideoModel> list = new ArrayList<VideoModel>();
        
        Cursor c = getCursor(db);
        if (c.moveToFirst()) {
            do {
                VideoModel video = DatabaseModelFactory.getModel(c);
                list.add(video);
            } while (c.moveToNext());
        }
        c.close();
        
        return list;
    }
    
    @Override
    public List<VideoModel> getDefaultValue() {
        return new ArrayList<VideoModel>();
    }
    
}
