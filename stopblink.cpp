#include <pigpio.h>

int main(int argc, char* argv[])
{
    gpioInitialise();
    int spiHandle = i2cOpen(1, 0x26, 0);
	i2cWriteByte(spiHandle, 3);
	gpioDelay(100);
    i2cClose(spiHandle);
    return 0;
}