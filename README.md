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
#### october 12th
Cool bug.

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/a12960e7-5131-4897-8f14-b5eb6b2763a7)
#### october 14th
I would kill this typo if I could.


![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/1eb44dd0-5ae1-440a-a1ff-243f99c8bcc0)

minY should obviously take the y value.
I had to deal with this typo the day before presenting my code, and I troubleshot it for at least an hour.
Nothing beats the frustration of "How could I miss this?"


When I said I would kill that typo I might've been exaggerating. But this bug I would actually kill if I could.


![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/97e49283-7458-412e-8e9a-b4e31ed01f08)


What you are looking at is the RGB map of surface normals.

The story of how I struggled for 5 hours with this bug shall remain undisclosed, but suffice it to say the problem was depth buffer testing. This might seem obvious to you but it stumped me. Until @Agab-dev suggested the genius idea that it could be triangles rendering in the back. Even then I doubted it, after all my cpp code worked perfectly without depth buffer testing. Later on we loaded a spherical model (thanks, MIT!) :

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/589f40a3-ce7f-41e3-801e-8cf4ac0fb292)

What a sickly ball!

Most importantly, I decided to test every axis separately.

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/d30d5174-08d3-4a39-bd16-db2c8f224db1)

The x-axis. Seems right.

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/fdcb82c5-afab-4d37-a4d2-555f4d0bc185)

Y. Seems right!

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/e599e5ce-8781-490b-aeaa-427da8f1f42b)

Curse you Z-axis.

After implementing depth testing : 

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/88678aab-be39-442b-aab1-5b7639d0833b)

What a healthy ball!



