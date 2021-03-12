package org.tomw.czeki.imageview;

import java.io.File;

public interface CheckImageNameDecoder {
    public boolean isCorrectFormat(String fileName);
    public boolean isCorrectFormat(File f);

    public boolean isValid(String fileName);
    public boolean isValid(File f);

    public boolean hasCorrectLength(String fileName);
    public boolean hasCorrectLength(File f);

    public boolean isFront(String fileName);
    public boolean isBack(String fileName);

    public boolean isFront(File f);
    public boolean isBack(File f);

    public String getCheckNumber(String fileName);
    public String getCheckNumber(File f);

    public String getSide(String fileName);
    public String getSide(File f);

}
