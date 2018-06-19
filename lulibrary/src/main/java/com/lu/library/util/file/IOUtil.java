package com.lu.library.util.file;



import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.channels.FileChannel;

/**
 * Created by lenovo on 2016/5/5.
 */
public class IOUtil {

    public static void writerString(String filePath, String data) throws IOException{
        BufferedWriter out = null;
        try {
            File file=new File(filePath);
            if (!file.exists()){
                file.createNewFile();
            }
            out = new BufferedWriter(new FileWriter(filePath, true));
            out.write(data);
            out.newLine();
            out.flush();
        } finally {
            closeStream(out);
        }
    }

    public static InputStream copyInputStream(InputStream inputStream) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        BufferedInputStream br = new BufferedInputStream(inputStream);
        byte[] b = new byte[1024];
        for (int c = 0; (c = br.read(b)) != -1; ) {
            bos.write(b, 0, c);
        }
        return new ByteArrayInputStream(bos.toByteArray());
    }

    private String encodeStream(InputStream in, String iencode) throws IOException {
        InputStreamReader reader;
//        LogUtil.d(IOUtil.class, ".....iencode:" + iencode);
        reader = new InputStreamReader(in, iencode);
        BufferedReader tBufferedReader = new BufferedReader(reader);
        StringBuffer tStringBuffer = new StringBuffer();
        String sTempOneLine = new String("");
        while ((sTempOneLine = tBufferedReader.readLine()) != null) {
            tStringBuffer.append(sTempOneLine);

        }
        return tStringBuffer.toString();
    }
    /**
     *
     */
    public static void closeStream(Object stream){
        if(stream==null){return;}
        try {
            if(stream instanceof Reader){
                ((Reader)stream).close();
            }else if(stream instanceof Writer){
                ((Writer)stream).close();
            }else if(stream instanceof InputStream){
                ((InputStream)stream).close();
            }else if(stream instanceof OutputStream){
                ((OutputStream)stream).close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String readerString(String filename) throws IOException{
//      Map<String,Object> map= HashMap.new
        BufferedReader read = null;
        StringBuffer result=new StringBuffer();
        try {
            read = new BufferedReader(new FileReader(filename));
            String data=null;

            while((data=read.readLine())!=null){
                result.append(data);
            }


        } finally {
            closeStream(read);
        }
        return result.toString();

    }
    public static void fileCopy(File in, File out) throws IOException {

        FileChannel inChannel = new FileInputStream(in).getChannel();

        FileChannel outChannel = new FileOutputStream(out).getChannel();
        try {
            int maxCount = (64 * 1024 * 1024) - (32 * 1024);
            long size = inChannel.size();
            long position = 0;
            while (position < size) {
                position += inChannel.transferTo(position, maxCount, outChannel);
            }

        } finally {

            if (inChannel != null) {

                inChannel.close();

            }

            if (outChannel != null) {

                outChannel.close();

            }

        }

    }
}