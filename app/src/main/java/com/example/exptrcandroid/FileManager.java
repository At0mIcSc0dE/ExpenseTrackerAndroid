package com.example.exptrcandroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import jcifs.smb.SmbException;
import jcifs.smb.SmbFileInputStream;
import jcifs.smb.SmbFileOutputStream;

public class FileManager {

    private String m_FilePath1;
    private String m_FilePath2;
    private String m_FilePath3;
    private String m_FilePath4;
    private String m_FilePathG;

    public HashMap<Integer, Vector<String>> m_OneTimeExpData;
    public HashMap<Integer, Vector<String>> m_MonthlyExpData;
    public HashMap<Integer, Vector<String>> m_OneTimeTakData;
    public HashMap<Integer, Vector<String>> m_MonthlyTakData;

    public GeneralData m_GeneralData;


    public FileManager(String filePath1, String filePath2, String filePath3, String filePath4, String filePathG) throws SmbException, MalformedURLException, UnknownHostException, IOException
    {
        m_FilePath1 = filePath1;
        m_FilePath2 = filePath2;
        m_FilePath3 = filePath3;
        m_FilePath4 = filePath4;
        m_FilePathG = filePathG;

        m_OneTimeExpData = new HashMap<Integer, Vector<String>>();
        m_MonthlyExpData = new HashMap<Integer, Vector<String>>();
        m_OneTimeTakData = new HashMap<Integer, Vector<String>>();
        m_MonthlyTakData = new HashMap<Integer, Vector<String>>();

        m_OneTimeExpData = Read(filePath1);
        m_MonthlyExpData = Read(filePath2);
        m_OneTimeTakData = Read(filePath3);
        m_MonthlyTakData = Read(filePath4);
        m_GeneralData = ReadGeneral();

    }

    private Vector<String> GetLines(String filePath)throws SmbException, MalformedURLException, UnknownHostException, IOException
    {
        Vector<String> lines = new Vector<>();
        Vector<Character> charArr = new Vector<>();
        SmbFileInputStream reader = new SmbFileInputStream(filePath);

        int readChar = 0;
        while((readChar = reader.read()) != -1)
        {
            if((char)readChar == '\r')
                continue;
            charArr.add((char)readChar);
        }
        reader.close();

        for(int i = 0; i < charArr.size(); ++i)
        {
            Vector<Character> buffer = new Vector<>();
            for(int j = i; j < charArr.size(); ++j)
            {
                if(charArr.get(j) == '\n')
                {
                    String vectorAsConcatenatedString = "";
                    for (Character c : buffer)
                        vectorAsConcatenatedString += c;
                    i += buffer.size();

                    lines.add(vectorAsConcatenatedString);
                    break;
                }
                else
                {
                    buffer.add(charArr.get(j));
                }
            }
        }

        return lines;
    }

    private HashMap<Integer, Vector<String>> Read(String filePath) throws SmbException, MalformedURLException, UnknownHostException, IOException
    {
        HashMap<Integer, Vector<String>> data = new HashMap<>();
        Vector<String> lines = GetLines(filePath);

        int index = -1;
        for(int i = 0; i < lines.size(); ++i)
        {
            if(lines.get(i).contains("#"))
            {
//                index = Character.getNumericValue(lines.get(i).toCharArray()[1]);
                StringBuilder sb = new StringBuilder(lines.get(i));
                sb.replace(0, 1, "");
                index = Integer.parseInt(sb.toString());

                data.put(index, new Vector<String>());
            }
            else
            {
                data.get(index).add(lines.get(i));
            }
        }
        return data;
    }

    public GeneralData ReadGeneral() throws SmbException, MalformedURLException, UnknownHostException, IOException
    {
        Vector<String> lines = GetLines(m_FilePathG);

        for(int i = 0; i < lines.size(); ++i)
        {
            switch(i)
            {
                case 0:
                    m_GeneralData.CurrOneTimeExpCount = Integer.parseInt(lines.get(i));
                    break;
                case 1:
                    m_GeneralData.CurrMonthlyExpCount = Integer.parseInt(lines.get(i));
                    break;
                case 2:
                    m_GeneralData.CurrOneTimeTakCount = Integer.parseInt(lines.get(i));
                    break;
                case 3:
                    m_GeneralData.CurrMonthlyTakCount = Integer.parseInt(lines.get(i));
                    break;
                case 4:
                    m_GeneralData.userID  = Integer.parseInt(lines.get(i));
                    break;
                case 5:
                    m_GeneralData.groupID = Integer.parseInt(lines.get(i));
                    break;
                case 6:
                    m_GeneralData.balance = Integer.parseInt(lines.get(i));
                    break;
                default:
                    break;
            }
        }

        return m_GeneralData;
    }

    public void WriteGeneral() throws SmbException, MalformedURLException, UnknownHostException, IOException
    {
        SmbFileOutputStream writer = new SmbFileOutputStream(m_FilePathG);

        StringBuilder sb = new StringBuilder();
        sb.append(("" + m_GeneralData.CurrOneTimeExpCount).toCharArray());
        sb.append(("" + m_GeneralData.CurrMonthlyExpCount).toCharArray());
        sb.append(("" + m_GeneralData.CurrOneTimeTakCount).toCharArray());
        sb.append(("" + m_GeneralData.CurrMonthlyTakCount).toCharArray());

        writer.write(sb.toString().getBytes());
    }

    public void AddToMap(ExpenseData data, Map<Integer, Vector<String>> map)
    {
        int nextIndex = 1;

        for(int i = map.size(); i > 0; --i)
        {

        }
    }

    public void RemoveFromMap(int expID, Map<Integer, Vector<String>> map)
    {

    }

}
