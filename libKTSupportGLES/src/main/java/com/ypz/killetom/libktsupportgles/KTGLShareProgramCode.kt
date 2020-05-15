package com.ypz.killetom.libktsupportgles

object KTGLShareProgramCode {

    const val KT_VERTEX_2D_SHADER = """
                uniform    mat4     u_Matrix;
                attribute  vec4     a_Position;
                attribute  vec2     a_TexCoord;
                varying    vec2     v_TexCoord;
                void main() {
                    v_TexCoord = a_TexCoord;
                    gl_Position = u_Matrix * a_Position;
                }
        """

    const val KT_FRAGMENT_2D_SHADER = """
                precision mediump float;
                varying vec2 v_TexCoord;
                uniform sampler2D u_TextureUnit;
                void main() {
                    gl_FragColor = texture2D(u_TextureUnit, v_TexCoord);
                }
                """

}