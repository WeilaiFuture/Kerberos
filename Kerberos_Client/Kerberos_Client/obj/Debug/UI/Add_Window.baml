   M S B A M L   `   `   `  ����J  FKerberos_Client, Version=1.0.0.0, Culture=neutral, PublicKeyToken=null7 clr-namespace:Kerberos_Client.UIKerberos_Client.UI  5   	   1clr-namespace:Kerberos_ClientKerberos_Client  5   	   ?$clr-namespace:Kerberos_Client.StructKerberos_Client.Struct  5   	   #    Kerberos_Client.UI.Add_Window   5   	   R NWindowsBase, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35W SPresentationCore, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35\ XPresentationFramework, Version=4.0.0.0, Culture=neutral, PublicKeyToken=31bf3856ad364e35D 9http://schemas.microsoft.com/winfx/2006/xaml/presentation    5      R NSystem.Xaml, Version=4.0.0.0, Culture=neutral, PublicKeyToken=b77a5c561934e089:x,http://schemas.microsoft.com/winfx/2006/xaml     5   	   @d2http://schemas.microsoft.com/expression/blend/2008     5   	   Jmc;http://schemas.openxmlformats.org/markup-compatibility/2006     5   	   2local clr-namespace:Kerberos_Client.UI     5   	   0local1clr-namespace:Kerberos_Client     5   	   7local2$clr-namespace:Kerberos_Client.Struct     5   	     � Title$  WndSrch��5
   	   $	��450��6   $	��800��6&    � 	Resources 5         Kerberos_Client.MyStruct 
  Friend9   3Kerberos_Client.Struct.StringToImageSourceConverter  MyImageConverter��%:   &	        &	        5   
   6+     5   
   6I   5      .��5      �  � RowDefinitions 5   
   �� 5      $��* �6   6)   �� 5      $��3* �6   6*   .+�5   
   � $��0��6   .+�5      �� $��
找人/群��6   $
��Left=�6#   $��
360,10,0,0q�6>   $	��Top=�6R   $��34��6j   $��78��6v   $��16(�6�   �� 5      -   5         ��Name$ Account��5      $
��Left=�6'   $��28��6B   $��
164,51,0,0q�6N    �� TextWrapping$
 Wrap=�6b   $	��Top=�6w   $	��437��6�   $��16(�6�   �� 5      $��Label��6   $
��Left=�6$   $��821,115,-127.4,0q�6?   $	��Top=�6Y   $	��100��6q   �� 5      -   5         ��Name$ BtnClr��5      $��搜索��6%   $
��Left=�62   $��
621,51,0,0q�6M   $	��Top=�6a   $��75��6y   $��28��6�   $��16(�6�   $
��Bold"�6�   ���	#FF1ACFD9���	#FFFCFCFC5   ,   �� 5      -   5        $��组建群聊��5      $
��Left=�6$   $��
709,51,0,0q�6?   $	��Top=�6S   $��75��6k   $��28��6v   $��16(�6�   $
��Bold"�6�   ���	#FF1ACFD9���	#FFFCFCFC5   ,   5      &   System.Windows.Controls.DataGrid  5   
   -   5          Name$ search_List��5   t   $��1��6      HeadersVisibility$
 None=�6       CanUserResizeColumns .  	   CanUserReorderColumns	 .  
   
IsReadOnly
 .    AutoGenerateColumns .   � DataContext# [  5   ?   $   HorizontalScrollBarVisibility$ Hidden=�5   #      GridLinesVisibility$ 
Horizontal=�6J      SelectionMode$ Single=�5    #      SelectionUnit$ FullRow=�6:      Columns 5!      4  .System.Windows.Controls.DataGridTemplateColumn  5#         CellTemplate 5$      �� 5%      .R�5&      ��  ��Name$ 
head_Image��6$   $��40��6�   $��40��6�    J� RenderTransformOrigin$	 1,1&�6�   ��68   �� *	U.Photo+ �� 	Converter# [  J� Clip 5'   "   @� 5(   &    @� RadiusX$ 60Q�66    @� RadiusY$ 60Q�6C    @� Center$ 30,30&�6P   5*      5+      5-        5/      ,  &System.Windows.Controls.DataGridColumn   Width5  /System.Windows.Controls.DataGridLengthConverter$ * 6)    50      �� 51      .R�52      ��  ��Name$ name_TextBlock��6(   ��6@   �� *	U.Uname+53      55        56      $ 40 6)    57      �� 58      .R�59      �� -   5        $��添加��59   %   6F   5:      5<      5>      5@      5A      