package org.openjfx.api2semestre.views_manager;

public class ViewsManager {
    private static View[] views;

    public static View[] getViews() {
        return views;
    }

    public static void setViews(View[] views) {
        ViewsManager.views = views;
    }


}
