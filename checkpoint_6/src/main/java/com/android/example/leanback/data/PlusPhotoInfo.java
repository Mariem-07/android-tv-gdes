package com.android.example.leanback.data;

/**
 * Created by maui on 18.11.14.
 */
public class PlusPhotoInfo {

    private Image image;
    private Cover cover;

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public Cover getCover() {
        return cover;
    }

    public void setCover(Cover cover) {
        this.cover = cover;
    }

    private class Image {
        private final String url;

        private Image(String url) {
            this.url = url;
        }


        public String getUrl() {
            return url;
        }
    }

    private class Cover {
        private CoverPhoto coverPhoto;

        private class CoverPhoto {
            private final String coverPhoto;

            private CoverPhoto(String coverPhoto) {
                this.coverPhoto = coverPhoto;
            }

            public String getCoverPhoto() {
                return coverPhoto;
            }
        }
    }
}
