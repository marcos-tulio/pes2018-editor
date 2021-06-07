package com.github.marcos.tulio.controller.util;

import com.jcraft.jzlib.Deflater;
import com.jcraft.jzlib.GZIPException;
import com.jcraft.jzlib.Inflater;
import com.jcraft.jzlib.JZlib;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Arrays;

/**
 *
 * @author MrCapybara
 */
public class ZlibTools {

    private String _descompressEnd = "_descompressed";
    private String _compressEnd = "_compressed";

    private String _localPath = "C:\\Users\\MrCapybara\\Desktop\\ZLIB";
    private String _fileName = "Country";
    private String _fileExtension = ".bin";

    private byte[] _header = {0x02, 0x10, (byte) 0x81, 0x57, 0x45, 0x53, 0x59, 0x53};

    public boolean descompress() {
        try {
            //Create a vector with all bytes of the file
            byte[] compress = Files.readAllBytes(new File(_localPath + File.separator + _fileName + _fileExtension).toPath());

            //Reads the size of files
            int compressLenght = ByteBuffer.wrap(compress, 8, 4).getInt();
            int descomprLenght = ByteBuffer.wrap(compress, 12, 4).getInt();

            //Remove header
            compress = Arrays.copyOfRange(compress, 16, compress.length);

            //Create output vector
            byte[] descompress = new byte[descomprLenght];

            //Descompress file
            Inflater inflater = new Inflater();
            inflater.setInput(compress);
            inflater.setOutput(descompress);

            while (inflater.total_out < descomprLenght && inflater.total_in < compressLenght) {
                inflater.avail_in = inflater.avail_out = 1;
                if (inflater.inflate(JZlib.Z_NO_FLUSH) == JZlib.Z_STREAM_END) {
                    break;
                }
            }

            inflater.end();
            //End descompress file

            FileOutputStream fos;
            //Save descompress file in a new file
            fos = new FileOutputStream(_localPath + File.separator + _fileName.concat(_descompressEnd) + _fileExtension);
            fos.write(descompress);
            fos.close();

            return true;
        } catch (GZIPException e) {
            return false;
        } catch (IOException | java.lang.ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    public boolean compress() {
        try {
            //Create a vector with all bytes of the file
            byte[] descompress = Files.readAllBytes(new File(_localPath + File.separator + _fileName.concat(_descompressEnd) + _fileExtension).toPath());

            //Create a vector that will receive output bytes
            byte[] compress = new byte[descompress.length];

            //Compress file
            Deflater deflater = new Deflater(JZlib.Z_BEST_COMPRESSION);
            deflater.params(JZlib.Z_BEST_COMPRESSION, JZlib.Z_DEFAULT_STRATEGY);

            deflater.setInput(descompress);
            deflater.setOutput(compress);

            while (deflater.total_in != descompress.length && deflater.total_out < compress.length) {
                deflater.avail_in = deflater.avail_out = 1; // force small buffers
                deflater.deflate(JZlib.Z_NO_FLUSH);
            }

            while (true) {
                deflater.avail_out = 1;
                if (deflater.deflate(JZlib.Z_FINISH) == JZlib.Z_STREAM_END) {
                    break;
                }
            }
            deflater.end();
            //End compress file

            //Remove clear bytes
            int offset;
            for (offset = (compress.length - 1); offset > 0; offset--) {
                if (compress[offset] != 0x00) {
                    offset++;
                    break;
                }
            }

            //Save bytes in a new file
            OutputStream stream = new FileOutputStream(_localPath + File.separator + _fileName.concat(_compressEnd) + _fileExtension);
            DataOutputStream out = new DataOutputStream(stream);

            //Save header
            out.write(_header);
            //Save compress size
            out.writeInt(compress.length);
            //Save descompress size
            out.writeInt(descompress.length);
            //Save compressed file
            out.write(compress, 0, offset);

            out.flush();
            out.close();
            stream.flush();
            stream.close();
            //End save file

            return true;
        } catch (GZIPException e) {
            return false;
        } catch (IOException | java.lang.ArrayIndexOutOfBoundsException ex) {
            return false;
        }
    }

    public boolean fileExist() {
        return new File(_localPath + File.separator + _fileName + _fileExtension).exists();
    }

    public void setLocalPath(String localPath) {
        this._localPath = localPath;
    }

    public void setFileName(String fileName) {
        this._fileName = fileName;
    }

    public void setFileExtension(String fileExtension) {
        this._fileExtension = fileExtension;
    }

    public void setHeader(byte[] header) {
        this._header = header;
    }

    public void setCompressedEnd(String compressEnd) {
        this._compressEnd = compressEnd;
    }

    public void setDescompressedEnd(String descompressEnd) {
        this._descompressEnd = descompressEnd;
    }
}
