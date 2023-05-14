package org.openjfx.api2semestre.data;

import org.openjfx.api2semestre.database.Data;

public class MemberRelation extends Data {

    private int id;
    private int usr_id;
    private int cr_id;
    
    public MemberRelation(int id, int usr_id, int cr_id) {
        this.id = id;
        this.usr_id = usr_id;
        this.cr_id = cr_id;
    }

    public int getId() { return id; }
    public int getUserId() { return usr_id; }
    public int getResultCenterId() { return cr_id; }

    // public void setId(int id) { this.id = id; }
    // public void setUserId(int usr_id) { this.usr_id = usr_id; }
    // public void setResultCenterId(int cr_id) { this.cr_id = cr_id; }

}
