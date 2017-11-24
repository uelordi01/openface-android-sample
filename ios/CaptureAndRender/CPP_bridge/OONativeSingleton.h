//
//  OOViulibSingleton.h
//
//  Created by Victor Goñi on 07/11/13.
//  Copyright (c) 2013 VictorTech. All rights reserved.
//

#import <Foundation/Foundation.h>
#include "../NativeCode/NativeCode.h"

@interface OONativeSingleton : NSObject
{
    Native* m_native;
    size_t m_frameHeight, m_frameWidth;
}

+(OONativeSingleton *) sharedInstance;


- (void)initSample;
// Frame update functions
- (void)processFrame:(cv::Mat&) frame;

- (void)reset;

@end
