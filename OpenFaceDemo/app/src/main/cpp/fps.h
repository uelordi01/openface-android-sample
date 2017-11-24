//
// Created by uelordi on 6/10/17.
//

#ifndef CAPTUREONLY_FPS_H
#define CAPTUREONLY_FPS_H
#include <ctime>
class Fps {
public:
    Fps() {
        total_time = 0;
        mIterations = 10;
        mFrameCounter = 0;
        mCurrentFps = 1;
    }
    ~Fps() {

    }
    void start() {
        time_start = std::time(0);
        time_end = std::time(0);
        mIterations = 10;
        mFrameCounter = 0;

    }
     double checkFps() {
        time_end = std::time(0);
        total_time = total_time + (time_end - time_start);
        if (mFrameCounter % mIterations == 0 ) {
            mCurrentFps = 1/(total_time/ mFrameCounter);
        }
         mFrameCounter++;
         time_start = time_end;
        return mCurrentFps;
    }
    void end() {

    }
private:
    int frame;
    std::time_t time_start;
    std::time_t time_end;
    double total_time;
    int mIterations;
    int mFrameCounter;
    double mCurrentFps;


};


#endif //CAPTUREONLY_FPS_H
