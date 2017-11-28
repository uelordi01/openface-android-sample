//
//  ViulibCommonSample.h
//  TrackingSample
//
//  Created by Jon Goenetxea on 17/11/14.
//  Copyright (c) 2014 VictorTech. All rights reserved.
//

#ifndef __TrackingSample__ViulibCommonSample__
#define __TrackingSample__ViulibCommonSample__

#include <opencv2/dnn.hpp>
#include <opencv2/imgproc.hpp>
#include <opencv2/highgui.hpp>

#include "LandmarkDetectorModel.h"
#include "LandmarkDetectorFunc.h"
#include "LandmarkDetectorParameters.h"
#include "LandmarkDetectorUtils.h"

#include "universalTimer.h"

class Native
{
private:
    std::string time_stamp(std::string format);
    
    LandmarkDetector::FaceModelParameters m_det_parameters;
    LandmarkDetector::CLNF m_clnf_model;
    
    UniversalTimer m_timer;
    float m_elapsedTimeSum;
    float m_meanElapsedTime;
    int m_elapsedTimeCont;
   
public:
    
    Native( std::string& resourcesPath );
    ~Native();
    
    void init(const std::string& parametersFile,
              const std::string& inputVideoName);
    
    // Frame update functions
    void processFrame(cv::Mat& frame);
    
private:
    std::string m_resourcesDirectoryPath;
};

#endif /* defined(__TrackingSample__ViulibCommonSample__) */
