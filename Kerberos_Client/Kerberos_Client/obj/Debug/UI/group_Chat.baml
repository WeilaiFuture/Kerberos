   M S B A M L   `   `   `  ����J  FKerberos_Client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null1clr-namespace:Kerberos_ClientKerberos_Client  5   	   ?$clr-namespace:Kerberos_Client.StructKerberos_Client.Struct  5   	   #    Kerberos_Client.UI.group_Chat   5   	   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5   	   R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   /localclr-namespace:Kerberos_Client     5   	   7local1$clr-namespace:Kerberos_Client.Struct     5   	     � Title$
  Chat��5	   
   $	��500��6   $	��600��6$    � 	Resources 5
      9   3Kerberos_Client.Struct.StringToImageSourceConverter   MyImageConverter  green_normal  green_normal_brush �� Color   Kerberos_Client.MyStruct  User��%�   &	        &	      &	 2     7  &	 J       5   
   6I   ��5   
   #07C160��6.   �� 5   
   8   65     5   
   6(   5      .��5      �  � ColumnDefinitions 5   
   �� 5      $��2* �6   6,   �� 5      $��* �6   6+   .+�5   
   ��  J� Opacity$	 0.6Q�6   $��2��6   $��	0,0,0.2,0q�63   ._�5      �� $��../Image_Source/back.jpg��6   $
��Fill=�67   $��0,-16,0.6,0.4q�6F   $��Right=�6]   $	��593��6y   5      � 5   
   $��0��6   $��	0,0,0.2,0q�6     � RowDefinitions 5      �� 5      $��4* �6    �� 5      $��* �6    .+�5      �� -   5        $��ListBoxChat��5        ListBoxItemChatStyle#��[ 6*   $
��Auto=�6f   $��Disabled=�6�   $��
0,0,-0.2,0q�6�   ��5      �� 5       �� 
TargetType" ��6   .�5       ��  �� Property � �� �� Value 5!      �� 5"   "   	 �� 
TargetType"	 ��62   .R�5#   &   �� 
 ��Name$
 Bd��6-   #��z��69   #��z��6e   #��z��6�    �� Padding$ 0q�6�   ._�5$   *   �� |�.  .��5%   .   �� 5&   +   5'   '   5(   #   5*      5+      5-      �� 5/      -   5         ��Name$ 	send_text��5/      $��1��6)   $	 0.7Q�66   $��2��6D   $��
0,0,-0.8,0q�6X    �� TextWrapping$
 Wrap=�6l   6�   �� 50      -   5        $��1��50      $��发送��68    J� RenderTransformOrigin$ 1.166,0.926&�6F   $��325,64,-0.2,0.4q�6k   ��51      �� 52      " ��6   .�53      ��  � ��
 � � �67   �� 54       � ��
 �����68   �� 55       � ��
 �����67   �� 56       � �� 57      �� 58   "   "	 ��62   .R�59   &   �� $
 PART_BUTTON_BORDER��6-   #��z��6I   $	��0.5q�6}    �� CornerRadius$ 3��6�   #��z��6�   ._�5:   *   �� #��z��6;   $��Center=�6n   $��Center=�6�   6�   5;   '   5<   #   5>       �� Triggers 5?      P� 5@       P� Property �@J�IsMouseOver P� Value . . �5A   "   ��  � ��
 � � �6?   �� 5B   "    �@�Cursor$
 Hand��6;   5C      P� 5D       � {� .  . �5E   "   ��  � ��
 �����6?   �� 5F   "    �@�Cursor$ No��6;   �� 5G   "    � ��
 �����6@   5H      5J      5L      5M      � 5N   
   $��1��6   $ 0.5,0.5&�6    $��10,0,-0.4,0.4q�6@    5O      �� 5P      $��2* �6    6.   �� 5Q      $��* �6    6-   .+�5S      � $��0��6   .+�5T      �� -   5        $��
head_Image��5T      $��../Image_Source/test.jpg��6*   $��22,26,38,186.6q�6L   z�5U      V� 5V      .~�5W      �� �� 5X       �� AngleX$ -0.239Q�6,   �� 5Y      T� 5Z       T� X$ -0.396Q�61   5[       J� Clip 5]      @� 5^       @� RadiusX$ 50Q�6*    @� RadiusY$ 50Q�67    @� Center$ 50,50&�6D   5`      �� 5a      -   5         ��Name$ Uname_TextBlock��5a      $��41��65   $	 0.6Q�6B   $��0,145,19,127.6q�6Q   6l   �� 5b      -   5        $ Uid_TX��5b      $��41��6,   $	 0.6Q�69   $��0,209,19,63.6q�6I   6c   5c      � 5d      $��1��6   .+�5e      &   System.Windows.Controls.DataGrid  -   5          Name$ 
group_List��5e   o      HeadersVisibility$
 None=�6      CanUserResizeColumns .     CanUserReorderColumns .     
IsReadOnly .    AutoGenerateColumns .    � DataContext#  [ 5f   #   $!   HorizontalScrollBarVisibility$! Hidden=�6G   "   GridLinesVisibility$" 
Horizontal=�6n   #   SelectionMode$# Single=�5g   #   $   SelectionUnit$$ FullRow=�6:   %   Columns% 5h      4  .System.Windows.Controls.DataGridTemplateColumn  5j      &   CellTemplate& 5k      �� 5l   "   .R�5m   &   �� ' ��Name$' 
head_Image��6,   $��20��6�   $��20��6�   ��6@   �� ( �� Path$( Photo�) �� 	Converter#) [   5n   *   @� 5o   .   $ 10Q�6>   $ 10Q�6K   $ 10,10&�6X   5q   '   5r   #   5t        5v      ,  &System.Windows.Controls.DataGridColumn*   Width5  /System.Windows.Controls.DataGridLengthConverter$* * 61   & 5w      �� 5x   "   .R�5y   &   � .+�5z   *   �� $ name_TextBlock��6A   $��0��64   ��6Y   �� *Uname+5{   '   5|   #   5~      5�      5�      5�      5�      5�      