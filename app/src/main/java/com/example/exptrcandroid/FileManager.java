package com.example.exptrcandroid;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.HashMap;
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


    public FileManager(String filePath1, String filePath2, String filePath3, String filePath4, String filePathG) throws IOException
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
        m_GeneralData = new GeneralData();

        m_OneTimeExpData = Read(filePath1);
        m_MonthlyExpData = Read(filePath2);
        m_OneTimeTakData = Read(filePath3);
        m_MonthlyTakData = Read(filePath4);
        m_GeneralData = ReadGeneral();

    }

    private Vector<String> GetLines(String filePath) throws IOException
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

    public ExpenseData ReadExpense(int expID, int expType)
    {
        ExpenseData expData = new ExpenseData("", 0, "", 0, 0, 0);

        switch(expType)
        {
            case 1:
                try
                {
                    expData.Name = m_OneTimeExpData.get(expID).get(0);
                }
                catch (Exception e)
                {
                    return expData;
                }

                expData.Price   = Double.parseDouble(m_OneTimeExpData.get(expID).get(1));
                expData.Info    = m_OneTimeExpData.get(expID).get(2);
                expData.Day     = Integer.parseInt(m_OneTimeExpData.get(expID).get(3));
                expData.Month   = Integer.parseInt(m_OneTimeExpData.get(expID).get(4));
                expData.Year    = Integer.parseInt(m_OneTimeExpData.get(expID).get(5));
                break;

            case 2:
                try
                {
                    expData.Name = m_MonthlyExpData.get(expID).get(0);
                }
                catch (Exception e)
                {
                    return expData;
                }

                expData.Price   = Double.parseDouble(m_MonthlyExpData.get(expID).get(1));
                expData.Info    = m_MonthlyExpData.get(expID).get(2);
                expData.Day     = Integer.parseInt(m_MonthlyExpData.get(expID).get(3));
                expData.Month   = Integer.parseInt(m_MonthlyExpData.get(expID).get(4));
                expData.Year    = Integer.parseInt(m_MonthlyExpData.get(expID).get(5));
                break;

            case 3:
                try
                {
                    expData.Name = m_OneTimeTakData.get(expID).get(0);
                }
                catch (Exception e)
                {
                    return expData;
                }

                expData.Price   = Double.parseDouble(m_OneTimeTakData.get(expID).get(1));
                expData.Info    = m_OneTimeTakData.get(expID).get(2);
                expData.Day     = Integer.parseInt(m_OneTimeTakData.get(expID).get(3));
                expData.Month   = Integer.parseInt(m_OneTimeTakData.get(expID).get(4));
                expData.Year    = Integer.parseInt(m_OneTimeTakData.get(expID).get(5));
                break;

            case 4:
                try
                {
                    expData.Name = m_MonthlyTakData.get(expID).get(0);
                }
                catch (Exception e)
                {
                    return expData;
                }

                expData.Price   = Double.parseDouble(m_MonthlyTakData.get(expID).get(1));
                expData.Info    = m_MonthlyTakData.get(expID).get(2);
                expData.Day     = Integer.parseInt(m_MonthlyTakData.get(expID).get(3));
                expData.Month   = Integer.parseInt(m_MonthlyTakData.get(expID).get(4));
                expData.Year    = Integer.parseInt(m_MonthlyTakData.get(expID).get(5));
                break;
        }

        return expData;
    }

    public void WriteExpense(int expType, ExpenseData data) throws IOException
    {
        switch (expType)
        {
            case 1:
                m_OneTimeExpData = AddDataToMap(data, m_OneTimeExpData);
                Write(m_FilePath1, m_OneTimeExpData);
                ++m_GeneralData.CurrOneTimeExpCount;
                break;
            case 2:
                m_MonthlyExpData = AddDataToMap(data, m_MonthlyExpData);
                Write(m_FilePath2, m_MonthlyExpData);
                ++m_GeneralData.CurrMonthlyExpCount;
                break;
            case 3:
                m_OneTimeTakData = AddDataToMap(data, m_OneTimeTakData);
                Write(m_FilePath3, m_OneTimeTakData);
                ++m_GeneralData.CurrOneTimeTakCount;
                break;
            case 4:
                m_MonthlyTakData = AddDataToMap(data, m_MonthlyTakData);
                Write(m_FilePath4, m_MonthlyTakData);
                ++m_GeneralData.CurrMonthlyTakCount;
                break;
        }
        WriteGeneral();
    }

    public void DeleteExpense(int expID, int expType) throws IOException
    {
        switch (expType)
        {
            case 1:
                m_OneTimeExpData = RemoveFromMap(expID, m_OneTimeExpData);
                --m_GeneralData.CurrOneTimeExpCount;
                Write(m_FilePath1, m_OneTimeExpData);
                break;
            case 2:
                m_MonthlyExpData = RemoveFromMap(expID, m_MonthlyExpData);
                Write(m_FilePath2, m_MonthlyExpData);
                --m_GeneralData.CurrMonthlyExpCount;
                break;
            case 3:
                m_OneTimeTakData = RemoveFromMap(expID, m_OneTimeTakData);
                Write(m_FilePath3, m_OneTimeTakData);
                --m_GeneralData.CurrOneTimeTakCount;
                break;
            case 4:
                m_MonthlyTakData = RemoveFromMap(expID, m_MonthlyTakData);
                Write(m_FilePath4, m_MonthlyTakData);
                --m_GeneralData.CurrMonthlyTakCount;
                break;
        }

        WriteGeneral();
    }

    public GeneralData ReadGeneral() throws IOException
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
                    m_GeneralData.balance = Double.parseDouble(lines.get(i));
                    break;
                default:
                    break;
            }
        }

        return m_GeneralData;
    }

    public void WriteGeneral() throws IOException
    {
        SmbFileOutputStream writer = new SmbFileOutputStream(m_FilePathG);

        StringBuilder sb = new StringBuilder();
        sb.append(("" + m_GeneralData.CurrOneTimeExpCount + "\n").toCharArray());
        sb.append(("" + m_GeneralData.CurrMonthlyExpCount + "\n").toCharArray());
        sb.append(("" + m_GeneralData.CurrOneTimeTakCount + "\n").toCharArray());
        sb.append(("" + m_GeneralData.CurrMonthlyTakCount + "\n").toCharArray());
        sb.append(("" + m_GeneralData.userID + "\n").toCharArray());
        sb.append(("" + m_GeneralData.groupID + "\n").toCharArray());
        sb.append(("" + m_GeneralData.balance + "\n").toCharArray());

        writer.write(sb.toString().getBytes());
    }

    public void UpdateIndices(int index, int expTime, boolean add) throws IOException
    {
        if (!add) {
            switch (expTime) {
                case 1:
                    m_GeneralData.CurrOneTimeExpCount -= 1;
                case 2:
                    m_GeneralData.CurrMonthlyExpCount -= 1;
                case 3:
                    m_GeneralData.CurrOneTimeTakCount -= 1;
                case 4:
                    m_GeneralData.CurrMonthlyTakCount -= 1;
            }
        }
        WriteGeneral();

        Vector<Integer> listOfAllIndices = new Vector<>();
        for (int i = 0; i < m_GeneralData.CurrOneTimeExpCount; ++i)
        {
            listOfAllIndices.add(i);
        }
        Collections.reverse(listOfAllIndices);

        int nextID = 0;
        switch (expTime)
        {
            case 1:
                nextID = m_GeneralData.CurrOneTimeExpCount;
            case 2:
                nextID = m_GeneralData.CurrMonthlyExpCount;
            case 3:
                nextID = m_GeneralData.CurrMonthlyTakCount;
            case 4:
                nextID = m_GeneralData.CurrMonthlyTakCount;
        }

        if(!add)
        {
            for(int i = index; i < nextID; ++i)
            {
                switch (expTime)
                {
                    case 1:
                        RemoveFromMap(i, m_OneTimeExpData);
                        Write(m_FilePath1, m_OneTimeExpData);
                    case 2:
                        RemoveFromMap(i, m_MonthlyExpData);
                        Write(m_FilePath2, m_MonthlyExpData);
                    case 3:
                        RemoveFromMap(i, m_OneTimeTakData);
                        Write(m_FilePath3, m_OneTimeTakData);
                    case 4:
                        RemoveFromMap(i, m_MonthlyTakData);
                        Write(m_FilePath4, m_MonthlyTakData);
                }
            }
        }
        else
        {
            for (int i = 0; i < nextID; ++i)
            {
                switch (expTime)
                {
                    case 1:
                        RemoveFromMap(i, m_OneTimeExpData);
                        Write(m_FilePath1, m_OneTimeExpData);
                    case 2:
                        RemoveFromMap(i, m_MonthlyExpData);
                        Write(m_FilePath2, m_MonthlyExpData);
                    case 3:
                        RemoveFromMap(i, m_OneTimeTakData);
                        Write(m_FilePath3, m_OneTimeTakData);
                    case 4:
                        RemoveFromMap(i, m_MonthlyTakData);
                        Write(m_FilePath4, m_MonthlyTakData);
                }
            }
        }

    }

    private HashMap<Integer, Vector<String>> Read(String filePath) throws IOException
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

    private void Write(String filePath, Map<Integer, Vector<String>> data) throws IOException
    {
        SmbFileOutputStream writer = new SmbFileOutputStream(filePath);
        for(int i = 1; i < data.size() + 1; ++i)
        {
            writer.write(("#" + String.valueOf(i) + "\n").getBytes());
            for(int j = 0; j < data.get(i).size(); ++j)
            {
                if(data.get(i).get(j) == "")
                {
                    writer.write("NULL\n".getBytes());
                    continue;
                }

                writer.write((data.get(i).get(j) + "\n").getBytes());
            }
        }
        writer.close();
    }

    public HashMap<Integer, Vector<String>> AddDataToMap(ExpenseData data, HashMap<Integer, Vector<String>> map)
    {
        int nextIndex = 1;
        HashMap<Integer, Vector<String>> temp = new HashMap<>();

        for(int i = map.size(); i > 0; --i)
        {
            temp.put(i + 1, map.get(i));
        }
        Vector<String> vec = new Vector<>();
        vec.add(data.Name);
        vec.add(String.valueOf(data.Price));
        vec.add(data.Info);
        vec.add(String.valueOf(data.Day));
        vec.add(String.valueOf(data.Month));
        vec.add(String.valueOf(data.Year));

        temp.put(1, vec);

        return temp;
    }

    public HashMap<Integer, Vector<String>> RemoveFromMap(int expID, Map<Integer, Vector<String>> map)
    {
        HashMap<Integer, Vector<String>> temp = new HashMap<>();

        map.remove(expID);
        for(int i = 1; i < map.size() + 1; ++i)
        {
            if(i < expID)
                temp.put(i, map.get(i));
            else
                temp.put(i, map.get(i + 1));
        }
        return temp;
    }

}
