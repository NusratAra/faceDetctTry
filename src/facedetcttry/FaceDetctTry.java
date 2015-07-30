package facedetcttry;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.FrameGrabber;
import com.googlecode.javacv.OpenCVFrameGrabber;
import static com.googlecode.javacv.cpp.opencv_core.*;
import static com.googlecode.javacv.cpp.opencv_objdetect.*;
import static com.googlecode.javacv.cpp.opencv_imgproc.*;
import static com.googlecode.javacv.cpp.opencv_highgui.*;

public class FaceDetctTry {

    public static void main(String[] args) {
        FrameGrabber grabber = null;
        try {
            grabber = new OpenCVFrameGrabber(0);
            grabber.start();
        } catch (Exception ex) {
            System.out.println("Camera can't start");
        }
        CanvasFrame canvas = new CanvasFrame("My Image");
        canvas.setDefaultCloseOperation(CanvasFrame.EXIT_ON_CLOSE);
        
       // CanvasFrame canvas2=new CanvasFrame("Blur");
        
        int bb = 0;
        while (true) {
            IplImage img = null;
            try {
                img = grabber.grab();


                //IplImage img1 = cvLoadImage(img);
                int newWidth = 320;
                int newHeight = 240;
                //make a blank image of desire dimension.
               // IplImage gray = IplImage.create(newWidth, newHeight, img.depth(), 1);
                IplImage small= IplImage.create(newWidth, newHeight, img.depth(), img.nChannels());
                cvResize(img, small);


                
                
                
                cvSmooth(small, small, CV_MEDIAN, 13);
                
               // cvShowImage("Blur", small);
               // canvas2.showImage(small);
                
                IplImage gray = IplImage.create(small.width(),small.height(), IPL_DEPTH_8U, 1);
                cvCvtColor(small, gray, CV_BGR2GRAY);
                cvEqualizeHist(gray, gray);
                String face_classifier = "haarcascade_frontalface_alt.xml";
                CvHaarClassifierCascade face_cascade = new CvHaarClassifierCascade(cvLoad(face_classifier));
                CvMemStorage storage = CvMemStorage.create();
                CvSeq faces = cvHaarDetectObjects(gray, face_cascade, storage, 1.1, 1, 0);
                for (int i = 0; i < faces.total(); i++) {
                    CvRect r = new CvRect(cvGetSeqElem(faces, i));
                    cvRectangle(img, cvPoint(r.x()*2, r.y()*2), cvPoint((r.x() + r.width())*2, (r.y() + r.height())*2), CvScalar.RED, 2, CV_AA, 0);
                    // int ss = (r.x() + r.width()) * (r.y() + r.height());
                    int a = r.x();
                    int b = r.y();
                    int c = r.width();
                    int d = r.height();
                    System.out.println(a + "    " + b + "    " + c + "      " + d);

                    //System.out.println(ss);
                    if (c > 100) {
                        bb++;
                        String aa = "save" + bb + ".jpg";
                        cvSaveImage(aa, small);
                    }
                }
                
                
                canvas.showImage(img);
//
            } catch (Exception ex) {
                System.out.println("Can't capture");
                break;
            }
        }
        try {
            grabber.stop();
        } catch (Exception ex) {
            System.out.println("Error");
        }

    }
}