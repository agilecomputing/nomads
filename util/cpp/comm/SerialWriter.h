/*
 * SerialWriter.h
 *
 * This file is part of the IHMC Util Library
 * Copyright (c) 1993-2014 IHMC.
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
 */

#ifndef INCL_SERIAL_WRITER_H
#define INCL_SERIAL_WRITER_H

#include "Writer.h"

namespace NOMADSUtil
{
    class Serial;

    class SerialWriter : public Writer
    {
        public:
            SerialWriter (Serial *pSerial, bool bDeleteWhenDone = false);
            ~SerialWriter (void);
            int writeBytes (const void *pBuf, unsigned long ulCount);

        protected:
            Serial *_pSerial;
            bool _bDeleteWhenDone;
    };
}

#endif   // #ifndef INCL_SERIAL_WRITER_H
