# The Rasterizer
Here will be the rasterizer code.

### Current status of Rasterizer :

* all testing done with a .PPM file.
* Renderer has ```setPixel(int x, int y, Color someColor)``` function.
* Renderer has ```drawLine(Pixel p0, Pixel p1, Color color)``` and ```drawTriangle(Pixel p0, Pixel p1, Pixel p2, Color color)``` functions.
* Renderer has Depth testing.
* [More functionality will be appended here]

### Development Diary : 

Converting C++ to Java is hell. Java doesn't let you do anything. I miss ``` const vec2& ``` T_T 

#### october 9th 
Note to future self and developers : when loading huge data into a string inside a for loop, please use stringBuilder or stringBuffer. I was copying the same string literally millions of times with megabytes worth of data.
#### october 10th
Finished setting up the basic renderer. Drawing 55 triangles took 7 seconds, seems kind of slow but won't know until we load actual model.
#### october 11th
Another one in the "typos that cost me hours" section 

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/f006499c-5418-4b6b-b6cd-c622f5c6f18a)

It should be ```if (std::abs(orthogonal.z)>1e-2)```

Erratic depth map 

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/7f9fe3a3-7112-4691-be62-c63a762cf7e5)

This is 3 errors working all at once. One is the typo above, the other is muliplting vertices in the wrong order to determine barycentric coordinates (this is why it looks triangulated), and the final error is that I forgot to initialize my depth buffer with the maximum negative value possible, instead leaving it to the default of 0. 

Correct (I hope!) depth map
![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/fec67688-9340-4f38-8e32-0eaf3961b4e1)
