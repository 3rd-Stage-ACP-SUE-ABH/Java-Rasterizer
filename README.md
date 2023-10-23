# The Rasterizer
This is (an attempt at) a real-time software rasterizer. The graphics logic is mainly based on the renderer described in [Tiny Renderer](https://github.com/ssloy/tinyrenderer/wiki/Lesson-0:-getting-started).

This project uses no dependencies other than the Java Class library.

### Current status of Rasterizer :

* ~All~ testing done with a .PPM file.
* Renderer has functions to draw lines, triangles, and set single pixels.
* Renderer has Depth testing.
* Renderer displays to window.
* Renderer has model loader.
* Renderer can load textures and interpolate texture coordinates.
* Window can display rendering output in real time
* Renderer has transformations.
* Shader can shade any number of directional light sources.
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

This is 3 errors working all at once. One is the typo above, the other is multiplying vertices in the wrong order to determine barycentric coordinates (this is why it looks triangulated), and the final error is that I forgot to initialize my depth buffer with the maximum negative value possible, instead leaving it to the default of 0. 

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

The story of how I struggled for 5 hours with this bug shall remain undisclosed, but suffice it to say the problem was depth buffer testing. This might seem obvious to you but it stumped me. Until [Agab](https://github.com/Agab-dev) suggested the genius idea that it could be triangles rendering in the back. Even then I doubted it, after all my cpp code worked perfectly without depth buffer testing. Later on we loaded a spherical model [(thanks, MIT!)](https://web.mit.edu/djwendel/www/weblogo/shapes/basic-shapes/sphere/sphere.obj) :

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

#### october 15th
In celebration of v0.1 of our rasterizer, here are some interesting images : 

Ball
![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/b65aeccb-2574-4c5a-ba28-59048a98b84b)


Interpolated normals
![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/a33b85cb-e9bd-4c69-b84d-147f38b4c9a7)

Non-interpolated normals
![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/b0fdfc0b-71ff-4fa0-b90d-46810b738882)


Texture sampling 


![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/359e1298-47bc-4957-8c44-5e359dd3affa)

This one might also feature in my bug report, I still don't know if this is what it should look like.


It turns out I was wrong. Everything was right. The bug eludes me. I just used my ```map()``` function, instead of doing the operations directly, and the texture loaded perfectly.
I don't understand, but this is no time for understanding. The code worked, hurrah!

![image](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/fb0c7f2c-c2c9-4e03-b070-b8f15ad50a42)


For future reference the texture was loading all fucked up when I didn't use ```map()```.


#### october 16th
Finally! we have real-time rendering, but...at what cost?
It's very slow. VERY.
Currently only outputting about 0.5FPS with a single model loaded. I don't even have any ideas on how to make it faster anymore, other than multithreading.
#### october 17th
I am lucky that I worked with OpenGL before. I was wondering why my model wasn't rendering every frame (even though I was clearing the color buffer), and it just occured to me that we also clear the depth buffer in OpenGL every frame.
Bless [LearnOpenGL](https://learnopengl.com/)!
#### october 18th
No real updates today except some cool gifs :


![ezgif-5-0261ae3332](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/fb7ca27f-b182-44c2-8da9-e3f467fc25d1)


![ezgif-5-b3d88de14d](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/d1be0a90-b37d-4654-9a48-8f8c34e6efb3)


![ezgif-5-713d5d5e29](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/4943b1e1-9728-4bb6-8136-cc9392742b0b)

Generally speaking, you get a lot of cool bugs in graphics, but this one really caught my eye. The most intentional looking bug I've had so far. I was forgetting to clear the color buffer every frame.

#### october 22nd
Long time no update!
Let's get started with this funny bug : 

![changingCoordsEveryFrame](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/7eb3f973-f4af-48e4-b541-418d37c6966a)

Instead of copying the vertex coordinates, I was changing them every frame, thus applying the same transform cumulatively.



Also, he's rotating.


![rotate](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/6683c68e-1e86-4ece-a848-80a6d3fe970e)

#### october 23rd
More rotaty ones : 

![cube-rotate](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/1aeea375-5dab-4ee5-9a12-151a860e6fc2)
![stanford-bunny-rotate-5x](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/591d3f9e-10d5-473d-a3e4-2a0044411153)
![african-head-rotation x4](https://github.com/3rd-Stage-ACP-SUE-ABH/ACP-Project/assets/50342436/87f5c23b-2373-432e-a93d-0a063c0ea873)

The cube gif is not sped up!

You can also notice that we have 2 light sources instead of the usual 1 + ambient. 
