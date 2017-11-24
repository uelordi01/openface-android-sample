//
//  ViulibCommonSample.cpp
//  TrackingSample
//
//  Created by Jon Goenetxea on 17/11/14.
//  Copyright (c) 2014 VictorTech. All rights reserved.
//

#include "NativeCode.h"

#include <stdio.h>
#include <sys/time.h>
#include <time.h>

#include <iostream>
#include <fstream>


using namespace std;
using namespace cv;

// some colors
cv::Scalar blueVicomtech(205,171,0);
cv::Scalar grayVicomtech(135,129,125);


Native::Native( std::string& resourcesPath )
{
    m_resourcesDirectoryPath = resourcesPath;
}

Native::~Native()
{
}


std::string Native::time_stamp(string format)
{
    std::string ts;
    //int pos;
    timeval te;
    char time_string[40];
    struct tm* ptm;
    
    gettimeofday(&te,0);
    ptm = localtime (&te.tv_sec);
    strftime (time_string, sizeof (time_string), format.c_str(), ptm);
    ts = time_string;
    
    return ts;
    
}

void Native::init(std::string& parametersFile, std::string& inputVideoName)
{
    // Load openface elements
    // The modules that are being used for tracking
    m_clnf_model = LandmarkDetector::CLNF(m_resourcesDirectoryPath + "/main_clnf_ibug_glasses_movile.txt");
    m_det_parameters.model_location = m_resourcesDirectoryPath + "/main_clnf_ibug_glasses_movile.txt";
    m_det_parameters.multi_view = false;
    m_det_parameters.track_gaze = false;
    m_det_parameters.validate_detections = false;
}


void Native::processFrame(cv::Mat& frame)
{
    cv::Mat greyImg;
    cv::cvtColor(frame, greyImg, CV_RGBA2GRAY);
    // Detect the landmarks with openface
    // Have provided bounding boxes
    bool success = LandmarkDetector::DetectLandmarksInVideo(greyImg,
                                                            m_clnf_model,
                                                            m_det_parameters);
    
    if (!success) {
        std::cout << "Error: face not found!" << std::endl;
        return;
    }
    
    cv::Mat xs = m_clnf_model.detected_landmarks(cv::Rect(0, 0, 1,
                                                        m_clnf_model.detected_landmarks.rows/2));
    cv::Mat ys = m_clnf_model.detected_landmarks(cv::Rect(0,
                                                        m_clnf_model.detected_landmarks.rows/2, 1,
                                                        m_clnf_model.detected_landmarks.rows/2));
    
    vector<cv::Point2f> lands;
    for (int i = 0; i < xs.rows; ++i) {
        lands.emplace_back(xs.at<double>(i), ys.at<double>(i));
    }
    
    size_t n = lands.size();
    for (size_t i = 0; i < n; ++i) {
        cv::circle(frame, lands[i], 1, cv::Scalar(0, 255 ,0));
    }
}

