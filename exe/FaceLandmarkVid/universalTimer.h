#ifndef UNIVERSAL_TIMER_H_
#define UNIVERSAL_TIMER_H_

#include <iostream>

// If it is linux system, define the functions we must use
#if __cplusplus <= 199711L
#ifdef __unix__
	#include <sys/time.h>
	#include <stdint.h>
	#include <stdbool.h>
	#include <stddef.h>
	#include <assert.h>
#elif defined __MACH__
    #include <sys/time.h>
    #include <stdint.h>
    #include <stdbool.h>
    #include <stddef.h>
    #include <assert.h>
#elif defined _WIN32
	#define NOMINMAX
        #include <string>
	#include <windows.h>
#elif defined _WIN64
        #define NOMINMAX
        #include <string>
        #include <windows.h>
#endif

class UniversalTimer
{
protected:
    long long m_init, m_global;
public:
    UniversalTimer(){ init(); }
    virtual ~UniversalTimer(){}

    /* Helpful conversion constants. */
    static const unsigned usec_per_sec = 1000000;
    static const unsigned usec_per_msec = 1000;

    static void QueryCounter(long long* performance_count)
    {
#ifdef __unix__
        struct timeval time;
        // Sanity check.
        assert(performance_count != NULL);
        // Grab the current time.
        gettimeofday(&time, NULL);
        *performance_count = time.tv_usec + // Microseconds.
                             time.tv_sec * usec_per_sec; // Seconds.
#elif defined __MACH__
        struct timeval time;
        // Sanity check.
        assert(performance_count != NULL);
        // Grab the current time.
        gettimeofday(&time, NULL);
        *performance_count = time.tv_usec + // Microseconds.
                             time.tv_sec * usec_per_sec; // Seconds.
#elif defined _WIN32
        LARGE_INTEGER timerFreq;
        LARGE_INTEGER c;
        QueryPerformanceFrequency(&timerFreq);
        QueryPerformanceCounter(&c);
        *performance_count = (c.QuadPart * 1000 / timerFreq.QuadPart );
#elif defined _WIN64
                LARGE_INTEGER timerFreq;
                LARGE_INTEGER c;
                QueryPerformanceFrequency(&timerFreq);
                QueryPerformanceCounter(&c);
                *performance_count = (c.QuadPart * 1000 / timerFreq.QuadPart );
#endif
    }

    void init()
    {
        long long current;
        UniversalTimer::QueryCounter(&current);
        m_global = current;
        m_init = m_global;
    }

    /**
     * Returns the elapsed time in seconds since the last init/split.
     */
    double getElapsedSeconds(std::string msg = "")
    {
        long long current;
        UniversalTimer::QueryCounter(&current);
        double secs = (double)(current - m_init) / usec_per_msec ;
#ifdef __MACH__
        secs *= 0.001f;
#elif __unix__
        secs *= 0.001f;
#endif
        if(msg.size() > 0)
            std::cout << msg << " ( time: " << secs << " seconds )" << std::endl;
        return secs;
    }

    /**
     * Returns the elapsed time (in seconds) since the last init/split
     */
    double splitSeconds(std::string msg = "")
    {
        long long current;
        UniversalTimer::QueryCounter(&current);
        double secs = (double)(current - m_init) / usec_per_msec ;
        m_init = current;
#ifdef __MACH__
        secs *= 0.001f;
#elif __unix__
        secs *= 0.001f;
#endif
        if(msg.size() > 0)
            std::cout << msg << " ( time: " << secs << " seconds )" << std::endl;
        return secs;
    }

    /**
     * Returns the elapsed time (in seconds) since the last init
     */
    double getTotalElapsedSeconds(std::string msg = "")
    {
        long long current;
        UniversalTimer::QueryCounter(&current);
        double secs = (double)(current - m_global) / usec_per_msec ;
        m_init = current;
        m_global = current;
#ifdef __MACH__
        secs *= 0.001f;
#elif __unix__
        secs *= 0.001f;
#endif
        if(msg.size() > 0)
            std::cout << msg << " ( time: " << secs << " seconds )" << std::endl;
        return secs;
    }
};

#else

#include <chrono>

// C++11 features are available
class UniversalTimer
{
protected:
    std::chrono::high_resolution_clock::time_point m_init, m_global;
public:
    UniversalTimer(){ init(); }
    virtual ~UniversalTimer(){}

    void init() {
        m_global = std::chrono::high_resolution_clock::now();
        m_init = m_global;
    }

    /**
     * Returns the elapsed time in seconds since the last init/split.
     */
    double getElapsedSeconds(std::string msg = "")
    {
        std::chrono::high_resolution_clock::time_point current;
        current = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double> time_span;
        time_span = std::chrono::duration_cast<std::chrono::duration<double>>(
                                                           current - m_init);
        double secs = time_span.count();
        if(msg.size() > 0)
            std::cout << msg << " ( time: " << secs << " seconds )" << std::endl;
        return secs;
    }

    /**
     * Returns the elapsed time (in seconds) since the last init/split
     */
    double splitSeconds(std::string msg = "")
    {
        std::chrono::high_resolution_clock::time_point current;
        current = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double> time_span;
        time_span = std::chrono::duration_cast<std::chrono::duration<double>>(
                                                           current - m_init);
        double secs = time_span.count();
        m_init = current;

        if(msg.size() > 0)
            std::cout << msg << " ( time: " << secs << " seconds )" << std::endl;
        return secs;
    }

    /**
     * Returns the elapsed time (in seconds) since the last init
     */
    double getTotalElapsedSeconds(std::string msg = "")
    {
        std::chrono::high_resolution_clock::time_point current;
        current = std::chrono::high_resolution_clock::now();
        std::chrono::duration<double> time_span;
        time_span = std::chrono::duration_cast<std::chrono::duration<double>>(
                                                           current - m_init);
        double secs = time_span.count();
        m_init = current;
        m_global = current;

        if(msg.size() > 0)
            std::cout << msg << " ( time: " << secs << " seconds )" << std::endl;
        return secs;
    }
};
#endif


#endif /* UNIVERSAL_TIMER_H_ */
