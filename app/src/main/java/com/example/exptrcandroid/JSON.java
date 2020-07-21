package com.example.exptrcandroid;

import android.view.View;
import org.json.*;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jcifs.smb.*;


public class JSON {

    JSONObject m_Obj;
    private String m_Url = "smb://192.168.178.45/share/Data2.json";

    public JSON() throws IOException
    {

        Map<String, String> map = new HashMap<>();
        map.put("name", "jon doe");
        map.put("age", "22");
        map.put("city", "chicago");
        m_Obj = new JSONObject(map);

//        String hello = "Hello";
//
//        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, null, null);
//        SmbFile smbFile = new SmbFile(m_Url, auth);
//        SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile);
//        smbFileOutputStream.write(m_Obj.toString().getBytes());
//        smbFileOutputStream.flush();
//        smbFileOutputStream.close();

    }

    public void Write(String data) throws IOException
    {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, null, null);
        SmbFile smbFile = new SmbFile(m_Url, auth);
        SmbFileOutputStream smbFileOutputStream = new SmbFileOutputStream(smbFile);
        smbFileOutputStream.write(data.getBytes());
        smbFileOutputStream.flush();
        smbFileOutputStream.close();
    }


}
