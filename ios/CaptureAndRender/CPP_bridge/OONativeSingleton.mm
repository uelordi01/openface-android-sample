//
//  OOViulibSingleton.m
//
//  Created by Victor Goñi on 07/11/13.
//  Copyright (c) 2013 VictorTech. All rights reserved.
//

#import "OONativeSingleton.h"

// Device model data
#import "UIDeviceHardware.h"

@implementation OONativeSingleton

// ******************************************************************************************
// * Singleton
//===========================================================================================
/**
 *  Function:       sharedInstance
 *  Description:    Basic Singleton pattern
 */
+(OONativeSingleton *) sharedInstance
{
    static OONativeSingleton *inst = nil;
    @synchronized(self)
    {
        if (!inst)
        {
            NSLog(@"Creating NativeSingleton...");
            inst = [[self alloc] init];
        }
    }
    return inst;
}

- (id) init
{
    self = [super init];
    if (self)
    {
        NSString *resourceDir = [[NSBundle mainBundle] resourcePath];
        std::string resourcePath = std::string([resourceDir UTF8String]);
        m_native = new Native(resourcePath);
        [self initSample];
    }
    return self;
}

- (void)dealloc
{
    if( m_native != NULL ) delete m_native;
    
}

- (void)initSample
{
    if(m_native)
    {
        std::string parameterFileName = "config.cfg";
        std::string inputVideoName = "";
        m_native->init(parameterFileName, inputVideoName);
    }
    else
    {
        NSLog(@"ERROR: Sample object not initialized!");
    }
}

- (void)processFrame:(cv::Mat&) frame;
{
    m_native->processFrame(frame);
}

- (void)reset
{
   
}


@end
