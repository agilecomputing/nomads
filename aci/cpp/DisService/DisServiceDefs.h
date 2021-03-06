/*
 * DisServiceDefs.h
 *
 * This file is part of the IHMC DisService Library/Component
 * Copyright (c) 2006-2014 IHMC.
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * version 3 (GPLv3) as published by the Free Software Foundation.
 *
 * U.S. Government agencies and organizations may redistribute
 * and/or modify this program under terms equivalent to
 * "Government Purpose Rights" as defined by DFARS 
 * 252.227-7014(a)(12) (February 2014).
 *
 * Alternative licenses that allow for use within commercial products may be
 * available. Contact Niranjan Suri at IHMC (nsuri@ihmc.us) for details.
 *
 * Author: Giacomo Benincasa    (gbenincasa@ihmc.us)
 * Created on February 2, 2011, 7:50 PM
 */

#ifndef INCL_DISSERVICE_DEFS_H
#define	INCL_DISSERVICE_DEFS_H

    #define checkAndLogMsg if (pLogger) pLogger->logMsg
    #define memoryExhausted Logger::L_Warning, "Memory exhausted.\n"
    #define coreComponentInitFailed Logger::L_SevereError, "component could not be initialized. Quitting.\n"
    #define listerRegistrationFailed Logger::L_SevereError, "%s could not be registered as a %s. Quitting.\n"
    #define bindingError Logger::L_SevereError, "binding error\n"

    #if defined (ANDROID)
        #define USE_LOGGING_MUTEX
    #endif

    #if defined (USE_LOGGING_MUTEX)
        #define MUTEX LoggingMutex
        #define logMutexId(className, mutexName, mutexId) if (pLogger) pLogger->logMsg (className, Logger::L_Info, "created mutex %s with log %u\n", mutexName, mutexId);
    #else
        #define MUTEX Mutex
        #define logMutexId(className, mutexName, mutexId)
    #endif

    #if defined (WIN32)
        #define snprintf _snprintf
    #endif
#endif	// INCL_DISSERVICE_DEFS_H

