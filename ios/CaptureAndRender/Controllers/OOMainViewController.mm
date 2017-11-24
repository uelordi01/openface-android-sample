//
//  OOViulibViewController.m
//
//  Created by Victor Goñi on 07/11/13.
//  Copyright (c) 2013 VictorTech. All rights reserved.
//

#import "OOMainViewController.h"

#import "OONativeSingleton.h"
#import "MBProgressHUD.h"

@interface OOMainViewController ()

@end

@implementation OOMainViewController

@synthesize cameraController = _cameraController;

- (id)initWithNibName:(NSString *)nibNameOrNil bundle:(NSBundle *)nibBundleOrNil
{
    self = [super initWithNibName:nibNameOrNil bundle:nibBundleOrNil];
    if (self) {
        // Custom initialization
    }
    return self;
}

- (void)viewDidLoad
{
    [super viewDidLoad];
    
    NSLog(@"--- Initial view did load; Initing...");
    _isFirstTime = true;
    [self initInterface];
    [self initViulibEngine];
    NSLog(@"--- Initial view did load; Initing done!");
    
}

- (void)dealloc
{
    // Declare camera manager and opengl context
    _cameraController = nil;
    
    // Views
    customCameraView = nil;
}

- (void)didReceiveMemoryWarning
{
    [super didReceiveMemoryWarning];
    // Dispose of any resources that can be recreated.
}

/**
 *  Function:       initInterface
 *  Description:    First step to load Viulib Singleton with all necessary elements
 */
-(void)initInterface
{
    NSLog(@"Initing View Interface...");

    NSLog(@"UI initialization");
    // Alloc View
    if( customCameraView == nil )
    {
        NSLog(@"Creating camera view...");
        // Generate camera controller instance
        customCameraView = [[UIView alloc] init];
        
        if( _cameraController == nil )
        {
            // Alloc Controllers
            _cameraController = [[OOCameraController alloc] init];
            [_cameraController setDelegate:self];
        }
        
        // Add the camera controller to the tree. Is necesary to add the view to the tree in order to be called for each frame
        [customCameraView addSubview:_cameraController.view];
        [self.view addSubview:customCameraView];
        [self.view bringSubviewToFront:customCameraView];
    }
    
    //UIDeviceOrientation ori = UIDeviceOrientationLandscapeLeft;
    UIDeviceOrientation ori = UIDeviceOrientationPortrait;
    //UIDeviceOrientation ori = UIDeviceOrientationLandscapeRight;
    [self setViewOrientation:ori];
    
    NSLog(@"Initing View Interface done!");
}


/**
 *  Function:       initViulibEngine
 *  Description:    Initiates the engine to be used later
 */
- (void) initViulibEngine
{
    NSString *deviceType = [UIDevice currentDevice].model;
    if([deviceType isEqualToString:@"iPhone"])
    {
        // InitSize
        NSLog(@"iPhone detected");
        
        [_cameraController adjustCaptureCameraOrientation:UIDeviceOrientationPortrait];
    }

}


- (NSUInteger) supportedInterfaceOrientations
{
    return UIInterfaceOrientationMaskPortrait /*| UIInterfaceOrientationMaskLandscapeLeft | UIInterfaceOrientationMaskLandscapeRight*/;
}

-(BOOL)shouldAutorotate
{
    return NO;
}


#pragma mark -
#pragma mark NewFrameDelegate

- (void) frameCaptured:(cv::Mat&) frame
{
    if(_isFirstTime)
    {
        dispatch_sync( dispatch_get_main_queue(),
                      ^{
                          // Show loading bar
                          MBProgressHUD *hud = [MBProgressHUD showHUDAddedTo:self.view animated:YES];
                          [hud setLabelText:@"Loading resources"];
                          dispatch_async(dispatch_get_global_queue( DISPATCH_QUEUE_PRIORITY_LOW, 0), ^{
                              [OONativeSingleton sharedInstance];
                              dispatch_async(dispatch_get_main_queue(), ^{
                                  // Hide loading bar
                                  [MBProgressHUD hideHUDForView:self.view animated:YES];
                              });
                          });
                      });
        
        _isFirstTime = false;
    }
    
    dispatch_sync( dispatch_get_main_queue(),
                  ^{
                      [[OONativeSingleton sharedInstance] processFrame:frame];
                      
                      // Correct frame channel ammounr
                      switch (frame.channels())
                      {
                          case 1:
                              cv::cvtColor(frame, frame, CV_GRAY2BGRA);
                              break;
                              
                          case 2:
                              cv::cvtColor(frame, frame, CV_BGR5652BGRA);
                              break;
                              
                          case 3:
                              cv::cvtColor(frame, frame, CV_RGB2RGBA);
                              break;
                      };
                      
                      [_renderView renderFrame:frame];
                      
    });
}


//***************************************************************//
//****          SMALL INTERFACE                             *****//
//***************************************************************//
-(int) switchCamera
{
    return [_cameraController toggleCamera];
}

-(void) setViewOrientation:(UIDeviceOrientation) ori
{
    [_cameraController setCameraOrientation:ori];
    [_renderView setRenderOrientation:ori];
}

@end
