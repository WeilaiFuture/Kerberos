   M S B A M L   `   `   `  ���� A  =Client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=nullclr-namespace:ClientClient      Client.MainWindow   W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     @d2http://schemas.microsoft.com/expression/blend/2008     Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     U QWpfAnimatedGif, Version=2.0.0.0, Culture=neutral, PublicKeyToken=9e7cd3b544a090dc,gif"http://wpfanimatedgif.codeplex.com   localclr-namespace:Client    � Title$  
MainWindow��$	��600��$	��800�� � WindowStyle$
 None=� � 
ResizeMode$ NoResize=� � 	Resources    Client.Number   
Key_Number��%   &	          .���  � RowDefinitions �� �� �� .+��� "  WpfAnimatedGif.ImageBehavior   AnimatedSource$ image/background.gif��$��-40,-40,-16.4,-29.6q�$��3�� J� RenderTransformOrigin$ 0.5,0.5&�z�V� .~��� ��  �� AngleY$ 0.139Q��� T�  T� Y$ 1.198Q�� $��0��$��Right=�$	��Top=�.+��� $��50��$��50��$��0,0,0,10q�.��� ���� 	 �� 
TargetType"	 ��.R��� #��z��$��1q�
 �� CornerRadius$
 7,7,7,7�����	#FFDDDDDD._��� #��z��$��Center=�$��Center=�� $��1��$��Center=� �� $	��400 ��� .+��� $��\image\test.jpg��$��Center=�$
��Auto��$	��190��$��63,20,578.6,66.8q�$��0��$��2��$ 0.5,0.5&�z�V� .~��� ��  �� AngleX$ -0.239Q��� T�  T� X$ -0.396Q� J� Clip @�  @� RadiusX$ 70Q� @� RadiusY$ 70Q� @� Center$ 70,70&��� .+�� $	��202��
������ $��	0,100,0,0q� �� �� �� $��50 � � ColumnDefinitions �� $
��159* ��� $
��158* ��� $
��158* ��� $
��204* ��� $
��114* �.+��� 
������� � DataContext# [   �� SelectedIndex$ 0��$��1��$
��Left=�$��66.8,15,0,0q�$	��Top=�$	��301��$��40��$��3�����	#FFC0EBCB �� 
IsEditable . $��18(����� *Items+ �� Source# [  �� $��记住密码��$
��Left=�$��142.8,20.8,0,-0.4q�$��2��$	��Top=�$��30��$	��115��$��16(�$��Oblique$����	#FFD7C73B$��2��$��1���� $��自动登陆��$��2��$
��Left=�$��127.4,20.8,0,-0.4q�$��2��$	��Top=�$��30��$ 0.438,1.063&�$��16(����	#FF9174BF$��2��G� $��1�� G� 	MaxLength$ 15��$
��Left=�$��
66.8,0,0,0q�$��1��$	��Top=�$��38��$��3��$	��301��$��18(��� $��3��$��Right=�$��
0,15,3.6,0q�$
��Wrap=�$�� ��$	��122��$��40��$	��Top=�$��18(��� -   $��http://www.baidu.com?�.��� $�注册账号���� $
��Left=�$��78,0,0,0q�$
��Wrap=�$�� ��$	��Top=�$��36��$	��122�����	#FFB8D60D$��18(�$��3��$��1���� -   $��http://www.baidu.com?�.��� $�找回密码��