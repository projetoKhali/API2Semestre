package org.openjfx.api2semestre.report;

public enum Week {
    Dom(new int[]{0}),
    Seg(new int[]{1}),
    Ter(new int[]{2}),
    Qua(new int[]{3}),
    Qui(new int[]{4}),
    Sex(new int[]{5}),
    Sab(new int[]{6}),
    ALL(new int[]{0,1,2,3,4,5,6}),
    UTEIS(new int[]{1, 2, 3, 4, 5}),
    FDS(new int[]{0,6});

    private int[] indexes;
    private Week (int[] indexes) {
        this.indexes = indexes;
    }

    public boolean[] get () {
        boolean[] result = new boolean[7];
        for (int i : indexes) {
            result[i] = true;
        }
        return result;
    }

    public boolean compare (boolean[] other) {
        boolean[] days = get();
        if (days.length != other.length) return false;
        for (int i = 0; i < days.length; i++) {
            if (days[i] != other[i]) return false;
        }
        return true;
    }


}
