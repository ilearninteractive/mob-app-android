package sa.gov.moe.etraining.model.api;

import java.io.Serializable;

import sa.gov.moe.etraining.interfaces.SectionItemInterface;

@SuppressWarnings("serial")
public class ChapterModel extends ResponseStatus implements Serializable,
        SectionItemInterface {
    public String name;
    public int videoCount;
    
    public boolean isIs_video_downloads_available() {
        return false;
    }

    public boolean isDownloaded_videos_chapter() {
        return false;
    }

    @Override
    public boolean isChapter() {
        return true;
    }

    @Override
    public boolean isSection() {
        return false;
    }
    
    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isCourse() {
        return false;
    }

    @Override
    public boolean isVideo() {
        return false;
    }

    @Override
    public boolean isDownload() {
        return false;
    }
}
