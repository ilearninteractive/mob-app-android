package sa.gov.moe.etraining.model.course;

import android.support.annotation.Nullable;

import sa.gov.moe.etraining.model.db.DownloadEntry;
import sa.gov.moe.etraining.module.storage.IStorage;

public interface HasDownloadEntry {
    @Nullable
    DownloadEntry getDownloadEntry(IStorage storage);

    @Nullable
    String getDownloadUrl();
}
